(ns app.todo
  (:require [reagent.core :as r]))

; {:123 {:id 123 :value "test" :done false}}
(def todo-list (r/atom (sorted-map)))

(defn add-todo [text]
  (let [created-at (js/Date.)
        id (.getTime created-at)]
    (swap! todo-list assoc id {:id id
                               :value text
                               :done false
                               :date created-at})))

(defn toggle-todo [{todo-id :id}]
  (swap! todo-list update-in [todo-id :done] not))

(defn toggle-all-todos []
  (let [update-one (fn [m done]
                     (reduce-kv #(assoc %1 %2 %3)
                                {:done done}
                                (dissoc m :done)))
        update-all (fn [done-status]
                     (swap! todo-list
                            (fn [x done]
                              (reduce-kv (fn [m k v] (->> done (update-one v) (assoc m k)))
                                         (empty x)
                                         x))
                            done-status))]
    (if (every? #(-> % :done true?) (vals @todo-list))
      (update-all false)
      (update-all true))))

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

(def todo-edit (with-meta todo-input
                 {:component-did-mount #(.focus (r/dom-node %))}))

(defn todo-component []
  (fn [{:keys [class style]}]
    (let [items (vals @todo-list)]
      [:div {:class class :style style}
       [:div.relative.border.border-solid
        [:button {:type "button"
                  :class "absolute pin-l pin-t h-full w-12 px-4 text-grey-light focus:outline-none"
                  :on-click toggle-all-todos}
         "✔"]
        [todo-edit {:initial-value ""
                    :class "block w-full text-xl pl-12 pr-4 py-4"
                    :placeholder "What needs to be done?"
                    :on-save add-todo}]]
       (when (-> items count pos?)
         [:ul.list-reset
          (for [todo items]
            [:li {:key (:id todo) :class "flex border-l border-r border-b border-solid"}
             [:button {:type "button"
                       :class "flex-none w-12 px-4 text-grey-light focus:outline-none"
                       :on-click #(toggle-todo todo)}
              "✔"]
             [:span
              {:class ["flex-auto block pr-4 py-3 text-xl" (when (:done todo) "text-grey line-through")]}
              (:value todo)]])])])))
