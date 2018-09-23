(ns app.router
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [shadow.loader :as loader]
            [app.components.core :as components]))

(def state "Router state" (atom {}))

(def app-routes
  ["/" {"" :home
        "todo" :todo}])

(def components
  {:home ((components/loadable
           #(loader/load "page-home")
           #((resolve 'app.pages.home/home-component))))
   :todo (components/with-header
           (components/loadable
            #(loader/load "page-todo")
            #((resolve 'app.pages.todo/todo-component))))})

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
