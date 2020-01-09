(ns roarer.thread.twitter.rest
  (:require [environ.core :refer [env]]
            [clojure.string :refer [join]]
            [oauth.digest :refer [hmac]]
            [oauth.signature :refer [rand-str]]
            [clj-http.client :as client])
  (:import (java.net URLEncoder)))

(def api-key (env :roarit-twitter-consumer-key))
(def api-secret (env :roarit-twitter-consumer-secret))
(def version "1.0")
(def signature-method "HMAC-SHA1")
(defn access-token [session] (session :oauth_token))
(defn access-token-secret [session] (session :oauth_token_secret))
(defn gen-timestamp [] (long (/ (System/currentTimeMillis) 1000)))
(defn gen-nonce [] (rand-str 30))

(defn percent-encode [input]
  (let [s (cond (keyword? input) (name input)
                (number? input) (str input)
                :else input)]
    (-> (URLEncoder/encode s "UTF-8")
        (.replace "+" "%20")
        (.replace "*" "%2A")
        (.replace "%7E" "~"))))

(defn key-value-percent-encode [v2] (vec (map percent-encode v2)))

(defrecord Api [method url params])
(defn statuses-update-api [params]
  (->Api :POST "https://api.twitter.com/1.1/statuses/update.json" params))

(defrecord Header
  [oauth_consumer_key oauth_nonce oauth_signature oauth_signature_method oauth_timestamp oauth_token oauth_version])
(defn header-with-no-sign
  "Creates MAP without oauth_signature. It does not return Header record."
  ([oauth-token]
   (dissoc (->Header api-key (gen-nonce) "" signature-method (gen-timestamp) oauth-token version) :oauth_signature)))
(defn make-header [^Api api session]
  "handles signature creation using Api data"
  (let [no-sign-header (header-with-no-sign (access-token session))
        m (concat (:params api) no-sign-header)
        encode (fn [v2] (str (v2 0) "=" (v2 1)))
        param-string (->> m
                          (map key-value-percent-encode)
                          (sort)
                          (map encode)
                          (join "&"))
        http-method (name (:method api))
        url-encoded (percent-encode (:url api))
        param-string-encoded (percent-encode param-string)
        signature-base-string (join "&" [http-method url-encoded param-string-encoded])
        signing-key (str api-secret "&" (access-token-secret session))
        oauth_signature (hmac signing-key signature-base-string)]
    (map->Header (conj no-sign-header {:oauth_signature oauth_signature}))))

(defn header-encode [^Header header]
  (let [encode (fn [v2] (str (v2 0) "=\"" (v2 1) "\""))]
    (->> header
         (map key-value-percent-encode)
         (map encode)
         (join ","))))

(defn gen-auth-header [^Header header]
  "Generate twitter auth (final) header"
  {:Authorization (str "OAuth " (header-encode header))})

(defn call [^Api api session]
  (let [auth-header (->> session (make-header api) (gen-auth-header))
        client-method (cond (= (:method api) :GET) client/get
                            (= (:method api) :POST) client/post)]
    (client-method (:url api) {:headers auth-header :form-params (:params api)})))