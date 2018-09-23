(ns app.router
  (:require [bide.core :as r]
            [rum.core :as rum]
            [app.todo :as todo]))

(def navigate! r/navigate!)

(def router
  (r/router [["/" :home]
             ["/todo" :todo]]))

(rum/defc home []
  [:div#home
   [:h1.mb-4 "Home"]
   [:nav [:a {:href "/todo"
              :on-click (fn [e] (.preventDefault e) (r/navigate! router :todo))}
          "Todo"]]])

(def routes
  {:home (home)
   :todo (todo/todo-component {:class "mx-auto shadow-lg"
                               :style {:max-width "400px"}})})

(def current-route (atom {}))

(defn get-component [route-data] ((:name route-data) routes))

(defn on-navigate
  "A function which will be called on each route change."
  ([name params query]
   (reset! current-route {:name name :params params :query query}))
  ([name params query on-change]
   (reset! current-route {:name name :params params :query query})
   (on-change)))

(defn start!
  ([]
   (r/start! router {:html5? true
                     :default :home
                     :on-navigate on-navigate}))

  ([{on-change :on-change}]
   (r/start! router {:html5? true
                     :default :home
                     :on-navigate #(on-navigate %1 %2 %3 on-change)})))
