 (defproject ascent/agent "0.0.1-SNAPSHOT"
  :description "Instrumentation Agent for ascent project"
  :url "https://github.com/suprematic/ascent.agent"
  
  :license { :name "Eclipse"
    :url "http://www.eclipse.org/legal/epl-v10.html"}
  
  :source-paths  ["src/cljs"]
  
  :dependencies [[org.clojure/clojure "1.5.1"]
                [org.clojure/clojurescript "0.0-2234"]
                [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                [khroma "0.0.1"]
                [prismatic/dommy "0.1.2"]
                [jarohen/chord "0.3.1"]]
  
  :plugins [[lein-cljsbuild "1.0.2"]]
  
  :cljsbuild {
    :builds[{:id "dev"
    :source-paths ["src/cljs"]
    :compiler {:output-dir "target/js/compiled"
      :output-to "target/js/compiled/ascent-agent.js"
      :source-map "target/js/compiled/ascent-agent.js.map"
      :optimizations :none
      :pretty-print true}}]})