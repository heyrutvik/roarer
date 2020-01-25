(ns roarer.util.oauth
  (:require [buddy.auth.backends.session :refer [session-backend]]
            [ring.util.http-response :refer [forbidden]]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]))

(defn unauthorized-handler [request metadata]
  (if (authenticated? request) forbidden (redirect "/login")))

(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))

(defn run-if-authenticated [session f & args]
  "takes session, f and varargs
  calls f only if call authenticated
  f takes session as first argument, and provided args if any"
  (if-not (authenticated? session)
    (throw-unauthorized)
    (apply f (cons session args))))