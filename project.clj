(defproject roarer "0.1.0-SNAPSHOT"
  :description "An app to publish a thread on twitter without hassle."
  :url "https://roarit.herokuapp.com/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.logging "0.5.0"]
                 [ring "1.8.0"]
                 [compojure "1.6.1"]
                 [environ "1.1.0"]
                 [clj-oauth "1.5.5"]
                 [buddy/buddy-auth "2.2.0"]
                 [metosin/ring-http-response "0.9.1"]
                 [ring/ring-json "0.5.0"]
                 [com.twitter.twittertext/twitter-text "3.0.1"]
                 [clj-http "3.10.0"]
                 [ring-cors "0.1.13"]
                 [io.aviso/pretty "0.1.37"]]
  :min-lein-version "2.0.0"
  :uberjar-name "roarer.jar"
  :plugins [[io.aviso/pretty "0.1.37"]]
  :middleware [io.aviso.lein-pretty/inject]
  :profiles {:production {:env {:production true}}})
