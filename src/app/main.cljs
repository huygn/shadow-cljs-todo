(ns app.main
  (:require [reagent.core :as r]
            [app.todo :as app]))

(defn render []
  (r/render [:main.min-h-screen.bg-white.font-sans.leading-normal.text-grey-darker
             [:div.container.px-4.pt-24.mx-auto
              [app/todo-component {:class "mx-auto shadow-lg"
                                   :style {:max-width "400px"}}]]]
            (js/document.getElementById "app")))

(defn ^:export init []
  (render))

(defn reload! []
  (js/console.log "reload")
  (init))
