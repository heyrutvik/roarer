(ns roarer.common.routes
  (:require [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET routes]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.util.response :refer [response redirect]]
            [roarer.oauth.twitter :as tw]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [roarer.util.oauth :refer [auth-backend run-if-authenticated]]
            [ring.util.http-response :refer [ok]]))

(defroutes common
  (GET "/" []
     (-> (ok {:message "Roarer backend running..."})))
  (GET "/api/logout" []                                         ;; Logout endpoint
     (-> (redirect "/") (assoc :session nil)))
  (route/resources "/"))