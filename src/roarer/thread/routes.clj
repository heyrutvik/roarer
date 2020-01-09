(ns roarer.thread.routes
  (:require [compojure.core :refer [defroutes POST]]
            [roarer.thread.core :refer [split publish]]
            [ring.util.response :refer [response]]
            [roarer.oauth.util :refer [run-if-authenticated]]))

(defroutes thread
  (POST "/thread" {session :session body :body}
    (run-if-authenticated session split (:content body)))
  (POST "/publish" {session :session body :body}
    (run-if-authenticated session publish (:thread body))))