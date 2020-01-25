(ns roarer.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.handler :refer [site]]
            [compojure.core :refer [routes]]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [roarer.oauth.routes :as oauth-routes]
            [roarer.common.routes :as common-routes]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [roarer.util.oauth :refer [auth-backend]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [roarer.thread.routes :as thread-routes]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.cors :refer [wrap-cors]]
            [roarer.util.middleware :refer [update-roarer-session-response set-roarer-session-request]]))

(def app
  (routes common-routes/common oauth-routes/twitter thread-routes/thread))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8081))
        front-end (env :roarit-front-end-domain)]
    (log/info (str "Starting the app at " port))
    (run-jetty
      (-> (site #'app)
          (wrap-reload)                                     ;; helpful in development
          (wrap-cors
            :access-control-allow-origin (re-pattern front-end)
            :access-control-allow-methods [:get :post])
          (wrap-authentication auth-backend)
          (wrap-authorization auth-backend)
          (wrap-json-response)
          (wrap-json-body {:keywords? true})
          (set-roarer-session-request)
          (update-roarer-session-response)) {:port port :join? false})))