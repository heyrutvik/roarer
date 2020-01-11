(ns roarer.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.handler :refer [site]]
            [compojure.core :refer [routes]]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [roarer.oauth.routes :as oauth-routes]
            [roarer.common.routes :as common-routes]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [roarer.oauth.util :refer [auth-backend]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [roarer.thread.routes :as thread-routes]
            [ring.middleware.reload :refer [wrap-reload]]))

(def app
  (routes common-routes/common oauth-routes/twitter thread-routes/thread))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8081))]
    (log/info (str "Starting the app at " port))
    (run-jetty
      (-> (site #'app)
          (wrap-reload)                                     ;; helpful in development
          (wrap-authentication auth-backend)
          (wrap-authorization auth-backend)
          (wrap-json-response)
          (wrap-json-body {:keywords? true})) {:port port :join? false})))