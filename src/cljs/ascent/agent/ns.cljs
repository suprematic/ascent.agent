(ns ascent.agent.ns
  (:require
    [ascent.log :as log]
    [dommy.core :as dommy])
  
  (:use-macros
    [dommy.macros :only [node sel1]]))

(defn load-script [src]
  (let [id (name (gensym))]
    (dommy/append! js/document.head
      (node [:script {:src src :async false :id id}]))
    (dommy/remove! (sel1 (str "#" id)))))

(defn clear-ns [ns]
  (when (.getObjectByName js/goog ns
    (let [parts  (clojure.string/split ns ".")
          name   (last parts)
          parent (butlast parts)]
      
      (.globalEval js/goog
        (if parent 
          (str "delete goog.getObjectByName('" (clojure.string/join "." parent) "')['" name "'];")
          (str "delete " name)))))))

(defn reload [ns]
  (let [ns (clojure.string/replace ns "-", "_")]
    (when (.isProvided_ js/goog ns)  
      (log/debug "reloading namespace: " ns)
        (when-let [path (.getPathFromDeps_ js/goog ns)]
          (clear-ns ns)
          (load-script (str (.-basePath js/goog) "/../" path))))))