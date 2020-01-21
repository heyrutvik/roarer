(ns roarer.common.routes
  (:require [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET routes]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.util.response :refer [response redirect]]
            [roarer.oauth.twitter :as tw]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [roarer.util.oauth :refer [auth-backend run-if-authenticated]]))

(defn- welcome-html [session]
  "Not so fancy html to welcome logged in user. Primarily for testing."
  (let [username (session :screen_name)]
    (response
      (str
        "<body>Welcome to Roarer, <i>" username "</i>!"
        "<br /><a href=\"/logout\">Logout</a></body>"))))

(defroutes common
  (GET "/" {session :session}                               ;; App main page endpoint
    (run-if-authenticated session welcome-html))
  (GET "/login" req                                         ;; Login endpoint
    (response
      (str "<a href=\"" (tw/oauth-init-uri req) "\">Login with Twitter</a>")))
  (GET "/api/logout" []                                         ;; Logout endpoint
     (-> (redirect "/") (assoc :session nil)))
  (route/resources "/"))