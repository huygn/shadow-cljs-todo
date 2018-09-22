(ns app.core
  (:require [sablono.util]))

(defn adapt-class [react-class]
  (fn [& args]
    (let [[opts children] (if (map? (first args))
                            [(first args) (rest args)]
                            [{} args])
          type# (first children)]
      (let [new-children (if (sequential? type#)
                           [(sablono.interpreter/interpret children)]
                           children)]

        (apply js/React.createElement react-class
               (clj->js (sablono.util/html-to-dom-attrs opts)) new-children)))))
