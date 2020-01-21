(ns roarer.thread.routes
  (:require [compojure.core :refer [defroutes POST]]
            [roarer.thread.core :refer [split publish]]
            [ring.util.response :refer [response]]
            [roarer.util.oauth :refer [run-if-authenticated]]))

(defroutes thread
  (POST "/api/thread" {session :session body :body}
    (run-if-authenticated session split (:content body)))
  (POST "/api/publish" {session :session body :body}
    (run-if-authenticated session publish (:thread body))))