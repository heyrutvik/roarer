(ns roarer.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]))

(defn handler [request-map]
  (response/response
    "<html><body>Welcome to Roarer!</body></html>"))

(defn -main [& args]
  (jetty/run-jetty
    handler
    {:port 8080}))