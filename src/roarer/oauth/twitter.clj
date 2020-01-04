(ns roarer.oauth.twitter
  (:require [oauth.client :as oauth]
            [environ.core :refer [env]]))

(def request-token-uri
  "https://api.twitter.com/oauth/request_token")

(def authenticate-uri
  "https://api.twitter.com/oauth/authenticate")

(def access-token-uri
  "https://api.twitter.com/oauth/access_token")

(def consumer
  (oauth/make-consumer (env :roarit-twitter-consumer-key)
                       (env :roarit-twitter-consumer-secret)
                       request-token-uri
                       access-token-uri
                       authenticate-uri
                       :hmac-sha1))

(defn oauth-callback-uri [{:keys [headers]}]
  (str (or (headers "x-forwarded-proto") "http")
       "://" (headers "host")
       "/oauth/twitter-callback"))

(defn oauth-init-uri [{:keys [headers]}]
  (str (or (headers "x-forwarded-proto") "http")
       "://" (headers "host")
       "/oauth/twitter-init"))

(defn fetch-request-token [request]
  (->> request
       oauth-callback-uri
       (oauth/request-token consumer)
       :oauth_token))

(defn auth-redirect-uri [request-token]
  (str (oauth/user-approval-uri consumer request-token)))

(defn fetch-access-token [request-token]
  (oauth/access-token consumer request-token (:oauth_verifier request-token)))