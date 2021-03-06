 (defproject
   ascent/agent "0.0.1-SNAPSHOT"
   :description "Ascent instrumentation agent"
   :url "https://github.com/suprematic/ascent.agent"

   :license { :name "EPL" :url "http://www.eclipse.org/legal/epl-v10.html"}

   :source-paths  ["src/cljs"]

   :dependencies [[org.clojure/clojure "1.5.1"]
                  [org.clojure/clojurescript "0.0-2234"]
                  [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                  [khroma "0.0.1"]
                  [prismatic/dommy "1.0.0"]
                  [jarohen/chord "0.3.1"]]
   )