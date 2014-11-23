(ns ascent.agent.ns
  (:require
    [ascent.log :as log]
    [dommy.core :as dommy :include-macros true])
  
  )

(defn load-script [src]
  (let [id (name (gensym))]
    (dommy/append! js/document.head
      (-> (dommy/create-element :script)
        (dommy/set-attr! :src src :async false :id id)))

    (dommy/remove! (dommy/sel1 (str "#" id)))))

(defn clear-ns [ns]
  (when (.getObjectByName js/goog ns
    (let [parts  (clojure.string/split ns ".")
          name   (last parts)
          parent (butlast parts)]
      
      ;FIXME don't delete sub-namespaces (check against goog ns dictionary)
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