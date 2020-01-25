(ns roarer.thread.routes
  (:require [compojure.core :refer [defroutes POST]]
            [roarer.thread.core :refer [split publish]]
            [ring.util.response :refer [response]]
            [roarer.util.oauth :refer [run-if-authenticated]]
            [clojure.tools.logging :as log]))

(defroutes thread
  (POST "/api/thread" {session :session body :body}
    (log/info "accessing: /api/thread")
    (log/info session)
    (run-if-authenticated session split (:content body)))
  (POST "/api/publish" {session :session body :body}
    (log/info "accessing: /api/publish")
    (log/info session)
    (run-if-authenticated session publish (:thread body))))