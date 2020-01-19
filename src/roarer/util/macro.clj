(ns roarer.util.macro
  (:require [clojure.tools.logging :as log]))

(defn- expand [bindings]
  (->> bindings
       (partition 2)
       (mapcat (fn [p]
                 (let [pair (vec p)]
                   `[~@pair _# (log/info (str "[" (first '~pair) " " (second '~pair) " " (second ~pair) "]"))])))
       vec))

(defmacro let-info [bindings & body]
  `(let ~(expand bindings) ~@body))