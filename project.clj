(defproject roarer "0.1.0-SNAPSHOT"
  :description "An app to publish a thread on twitter without hassle."
  :url "http://roarer.it"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring "1.8.0"]
                 [compojure "1.6.1"]
                 [environ "1.1.0"]]
  :main ^:skip-aot roarer.core
  :plugins [[environ/environ.lein "0.3.1"]]
  :uberjar-name "roarer.jar"
  :profiles {:production {:env {:production true}}})
