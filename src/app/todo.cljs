(ns app.todo
  (:require [rum.core :as rum]))

(rum/defc todo-component [text]
  [:div {:class "label"} text])
