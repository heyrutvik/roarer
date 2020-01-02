(ns roarer.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [compojure.core :as compojure]))

(defn response-handler [request-map]
  (response/response
    "<html><body>Welcome to Roarer!</body></html>"))

(compojure/defroutes handler
  (compojure/GET "/" req response-handler))

(defn -main [& args]
  (jetty/run-jetty
    handler
    {:port 8080}))