(ns roarer.oauth.unauthorized
  (:require [buddy.auth.backends.session :refer [session-backend]]
            [ring.util.http-response :refer [forbidden]]
            [ring.util.response :refer [redirect]]
            [buddy.auth :refer [authenticated?]]))

(defn unauthorized-handler [request metadata]
  (if (authenticated? request) forbidden (redirect "/login")))

(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))