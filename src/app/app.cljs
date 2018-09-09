(ns app.app
  (:require [reagent.core :as r]))

(def todo-list (r/atom []))

(defn add-todo [todo]
  (let [created-at (js/Date.)]
    (swap! todo-list conj
           {:value todo
            :done false
            :id (.getTime created-at)
            :date created-at})))

(defn toggle-todo [todo]
  (swap! todo-list
         (fn [todo-list todo-id]
           (map (fn [todo]
                  (if (= (:id todo) todo-id)
                    (update-in todo [:done] not)
                    todo))
                todo-list))
         (:id todo)))

(defn todo-input [{:keys [initial-value on-save]}]
  (let [value (r/atom initial-value)
        clear #(reset! value "")
        save #(let [v (-> @value str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (clear))]
    (fn [{:keys [class placeholder]}]
      [:input {:value @value
               :class class
               :placeholder placeholder
               :on-change #(reset! value (-> % .-target .-value))
               :on-key-down #(case (.-key %)
                               "Enter" (save)
                               "Escape" (clear)
                               nil)}])))

(defn todo-component []
  (fn [{:keys [class style]}]
    [:div {:class class :style style}
     [todo-input {:initial-value ""
                  :class "block w-full text-xl px-6 py-4 border border solid border-grey rounded"
                  :placeholder "What needs to be done?"
                  :on-save add-todo}]
     [:ul.list-reset
      (for [todo @todo-list]
        [:li {:key (:id todo) :class "py-2"}
         [:button {:type "button"
                   :class "mr-2"
                   :on-click #(toggle-todo todo)}
          "✔"]
         [:span
          {:class [(when (:done todo) "line-through text-grey")]}
          (:value todo)]])]]))
