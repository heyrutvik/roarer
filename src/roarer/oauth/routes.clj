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
    (-> (found "http://localhost:8080/login") (assoc :flash {:denied true})) ;; TODO remove static URL
    (let [access-token (tw/fetch-access-token request-token)
          user-id (access-token :user_id)
          user-info (dissoc access-token :user_id)]
      ;; session keys are underscore_separated, not hyphen-separated.
      (-> (found "http://localhost:8080/") (assoc :session (conj session user-info {:identity user-id})))))) ;; TODO remove static URL

(defroutes twitter
  (GET "/oauth/twitter-init" req (twitter-init req))        ;; endpoint to initialize twitter auth
  (GET "/oauth/twitter-callback" [& req-token :as req]      ;; twitter auth redirects to this endpoint
    (twitter-callback req-token req)))