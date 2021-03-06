(ns roarer.thread.core
  (:require [clojure.string :as string]
            [environ.core :refer [env]]
            [ring.util.http-response :refer [ok internal-server-error]]
            [roarer.thread.twitter.rest :as twitter-rest]
            [cheshire.core :refer [parse-string]])
  (:import (com.twitter.twittertext TwitterTextParser)))

(declare validTweet?)

;; Box is simple container to manipulate data in reduction steps.
(defrecord Box [index tweets])
(defn- make-box
  ([] (make-box 0 [""]))
  ([index tweets] (->Box index tweets)))
(defn- get-tweet [^Box b]
  "pick a tweet at index"
  ((:tweets b) (:index b)))
(defn- replace-tweet [^Box b tweet]
  "replace provided tweet at current index"
  (let [tweets (update (:tweets b) (:index b) (fn [_] tweet))]
    (make-box (:index b) tweets)))
(defn- thread [^Box b w]
  "we pass `chunk` to reducer to split content into valid tweet"
  (let [so-far (str (get-tweet b) " " w)]
    (if (validTweet? so-far)
      (replace-tweet b so-far)
      (make-box (inc (:index b)) (conj (:tweets b) w)))))

(defn- write-folio [total]
  "writes tweet number out of total"
  (if (= total 1)
    (fn [_ twit] twit)
    (fn [idx twit] (str twit " [" (inc idx) "/" total "]"))))

(defn- words [content]
  "split content on space"
  (string/split content #" "))

(defn- validTweet? [content]
  "content is valid tweet if
  1) it's valid according to TwitterTextParseResults
  2) its length should be lte to env length var"
  (let [tweet (TwitterTextParser/parseTweet content)
        length (Integer. (env :roarit-tweet-length))]
    (and (.-isValid tweet) (<= (.-weightedLength tweet) length))))

(defn split [session content]
  "split content into valid tweets"
  (let [box (reduce thread (make-box) (words content))
        length (inc (:index box))
        tweets (:tweets box)]
    (map-indexed (write-folio length) tweets)))

(defn publish [session thread]
  "publish thread to twitter"
  (let [result (reduce (fn [reply-to tweet]
                         (let [params (if (nil? reply-to) {:status tweet} {:status tweet :in_reply_to_status_id reply-to})
                               {:keys [status body]} (twitter-rest/call (twitter-rest/statuses-update-api params) session)
                               id ((parse-string body) "id")]
                           (if (= status 200) id (reduced nil)))) nil thread)]
    (if (nil? result)
      (internal-server-error {:message "Couldn't roar your thoughts, sorry!"})
      (ok {:message "Roared it!"}))))