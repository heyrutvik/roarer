(ns roarer.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET]]
            [environ.core :refer [env]]))

(defroutes app
  (GET "/" []
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "Welcome to Roarer!"}))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8080))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))