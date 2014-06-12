(ns ascent.agent.core
  (:require 
    [clojure.walk :as cw]
    [khroma.log :as log]
   
    [cljs.core.async :as async]
    [chord.client :as chord]
    [dommy.core :as dommy])

  
  (:require-macros 
    [dommy.macros :refer [node sel1]]
    [cljs.core.async.macros :refer [go alt! go-loop]]))

(defn- response-text [request]
  (.-responseText request))

(def out-channel 
  (async/chan))

(def in-channel 
  (async/chan))

(def in-pub 
  (async/pub in-channel :type))

(def connect-channel
  (async/chan 1))

(defn- timeout [ch timeout]
  (go
    (first
      (alts!
        [ch (async/timeout timeout)]))))

(defn- process-message [ch]
  (go  
    (if-let [message (<! ch)]
      (do
        (if (:error message)
          (log/warn "incoming message error: " (:error message))
          (>! in-channel (:message message))) true)
    
      (do
        (>! connect-channel "reconnect") false))))

(defn- connect-attempt [url]
  (go 
    (if-let [ch (<! (timeout (chord/ws-ch url) 2000))]
      (do
        (log/debug "connection opened: " ch)
        (async/pipe out-channel ch)
        (go-loop []
          (when (<! (process-message ch))
            (recur))))

      (>! connect-channel "timeout"))))


(defn- set-type [in type]
  (async/map< #(assoc % :type type) in))

(defn route-through [requests out-type in-type]
  (let [ch (async/chan)]
    (async/pipe (set-type requests out-type) out-channel) 
    (async/sub in-pub in-type ch) ch))

(defn subscribe [in-type]
  (let [ch (async/chan)]
    (async/sub in-pub in-type ch) ch))

(defn get-server-url []
  (if-let [element (sel1 "meta[name=AscentServerURL]")]
    (dommy/attr element :content)))

(defn connect [{:keys [url]}]
  (log/debug "conecting to ascent server: " url)


  
  (go-loop []
    (let [reason (<! connect-channel)]
      (log/info "connection attempt to " url ", reason: " reason)     
      (connect-attempt url)
      (recur)))
  
  (go
    (>! connect-channel "initial")))




