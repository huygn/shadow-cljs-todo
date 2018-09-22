(ns app.main
  (:require [rum.core :as rum]
            [app.core :as core]
            [app.todo :as app]
            [app.router :as router]))

(rum/defc app < rum/reactive []
  [:main.min-h-screen.bg-white.font-sans.leading-normal.text-grey-darker
   [:div.container.px-4.pt-24.mx-auto
    (router/get-component (rum/react router/current-route))]])

(defn render []
  (rum/mount (app) (js/document.getElementById "app")))

(defn ^:export init []
  (router/start!)
  (render))

(defn reload! []
  (js/console.log "reload")
  (init))
