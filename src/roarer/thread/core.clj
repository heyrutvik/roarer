(ns roarer.thread.core
  (:require [clojure.string :as string]))

(defn split [content]
  "split content into valid tweets"
  (string/split content #"\."))