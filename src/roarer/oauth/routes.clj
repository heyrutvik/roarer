(ns roarer.oauth.routes
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok found]]
            [roarer.oauth.twitter :as tw]))

(defn- twitter-init [request]
  "initialize twitter auth"
  (-> (tw/fetch-request-token request)
      tw/auth-redirect-uri
      found))

(defn- twitter-callback [request-token {:keys [session]}]
  "Determines what to do based on the response of twitter auth"
  (if (:denied request-token)
    (-> (found "/") (assoc :flash {:denied true}))
    (let [{:keys [user_id screen_name]} (tw/fetch-access-token request-token)]
      (-> (found "/") (assoc :session (assoc session :identity user_id :screen-name screen_name))))))

(defroutes twitter
  (GET "/oauth/twitter-init" req (twitter-init req))        ;; endpoint to initialize twitter auth
  (GET "/oauth/twitter-callback" [& req-token :as req]      ;; twitter auth redirects to this endpoint
    (twitter-callback req-token req)))