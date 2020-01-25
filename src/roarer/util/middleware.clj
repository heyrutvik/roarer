(ns roarer.util.middleware
  (:require [clojure.string :as string]
            [ring.middleware.cookies :refer [cookies-request]]))

(def set-cookie-key "Set-Cookie")
(def location-key "Location")
(def uuid-pattern #"[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}")

(defn- is-redirecting-to-success? [headers]
  (and (contains? headers location-key) (string/includes? (headers location-key) "success")))

(defn- get-session-id [headers]
  (re-find uuid-pattern (first (headers set-cookie-key))))

(defn- update-location [headers]
  (let [set-session #(string/replace % uuid-pattern (get-session-id headers))]
    (update-in headers [location-key] set-session)))

(defn update-roarer-session-response [handler]
  "replace success uuid with ring-session uuid, Heroku hack!"
  (fn [request]
    (let [response (handler request)]
      (if (is-redirecting-to-success? (:headers response))
        (update-in response [:headers] update-location)
        response))))

(defn is-requesting-api? [uri]
  (string/includes? uri "/api"))

(defn update-cookies [cookies]
  (update-in cookies ["ring-session"] (fn [_] (cookies "roarer-session"))))

(defn set-roarer-session-request [handler]
  "set ring-session on calling api, Heroku hack!"
  (fn [request]
    (handler (if (is-requesting-api? (:uri request))
               (let [request-map (cookies-request request)]
                 (update-in request-map [:cookies] update-cookies))
               request))))