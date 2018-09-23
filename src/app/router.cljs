(ns app.router
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [app.components.core :as components]
            [app.pages.todo :as todo]
            [app.pages.home :as home]))

(def state "Router state" (atom {}))

(def app-routes
  ["/" {"" :home
        "todo" :todo}])

(def components
  {:home (home/home-component)
   :todo (components/with-header todo/todo-component)})

(defn get-component [route-state] (:component route-state))

(defn set-page! [match]
  (println match)
  (swap! state assoc
         :page match
         :component ((:handler match) components)))

(def history
  (pushy/pushy set-page! (partial bidi/match-route app-routes)))

(defn start! []
  (pushy/start! history))
