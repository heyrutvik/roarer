(ns roarer.core
  (:gen-class)
  (:require [ring.adapter.jetty :as jetty]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [ring.middleware.resource :refer [wrap-resource]]))

(defroutes app
  (GET "/" []
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "Welcome to Roarer!"})
  (route/resources "/"))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8080))]
    (log/info (str "Starting the app at " port))
    (jetty/run-jetty
      (-> (site #'app)
          (wrap-resource "public")) {:port port :join? false})))