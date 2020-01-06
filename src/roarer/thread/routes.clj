(ns roarer.thread.routes
  (:require [compojure.core :refer [defroutes POST]]
            [roarer.thread.core :refer [split]]
            [ring.util.response :refer [response]]))

(defroutes thread
  (POST "/thread" {body :body}
    (response (split (:content body)))))