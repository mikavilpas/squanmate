(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.monochrome :as monochrome]))

(defn algorithm-visualization [puzzle alg-string]
  (let [steps (execution/transformations puzzle alg-string)
        key (gensym)]
    [:div
     (for [[step-either index] (zipmap steps (range))]
       ^{:key (str key index)}
       [:div
        (either/branch step-either
                       (fn [step]
                         (println "error encountered: " step)
                         [:div (pr-str step)])

                       (fn [step]
                         [monochrome/monochrome-puzzle (:puzzle step)]))])]))
