(ns roarer.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.handler :refer [site]]
            [compojure.core :refer [routes]]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [roarer.oauth.routes :as oauth-routes]
            [roarer.common.routes :as common-routes]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [roarer.oauth.unauthorized :refer [auth-backend]]))

(def app (routes common-routes/base oauth-routes/twitter))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8080))]
    (log/info (str "Starting the app at " port))
    (run-jetty
      (-> (site #'app)
          (wrap-authentication auth-backend)
          (wrap-authorization auth-backend)) {:port port :join? false})))