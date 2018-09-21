(ns app.main
  (:require [rum.core :as rum]
            [app.todo :as app]))

(rum/defc app []
  [:main.min-h-screen.bg-white.font-sans.leading-normal.text-grey-darker
   [:div.container.px-4.pt-24.mx-auto
    (app/todo-component "hello" {:class "mx-auto shadow-lg"
                                 :style {:max-width "400px"}})]])

(defn render []
  (rum/mount (app) (js/document.getElementById "app")))

(defn ^:export init []
  (render))

(defn reload! []
  (js/console.log "reload")
  (init))
