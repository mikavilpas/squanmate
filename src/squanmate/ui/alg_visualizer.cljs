(ns squanmate.ui.alg-visualizer
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.ui.drawing.monochrome :as monochrome]))

(defn- interesting-step? [step-either]
  (either/branch step-either
                 ;; errors must always be shown
                 (constantly true)
                 (fn [s]
                   (let [interesting #{execution/StartingStepResult
                                       execution/SliceStepResult}]
                     (interesting (type s))))))

(defn algorithm-visualization [puzzle alg-string]
  (let [steps (execution/transformations puzzle alg-string)]
    [:div
     alg-string
     [:div
      (for [[step-either index] (zipmap steps (range))
            :when (interesting-step? step-either)]
        ^{:key (str index)}
        [:div
         (either/branch step-either
                        (fn [step]
                          (println "error encountered: " step)
                          [:div (pr-str step)])

                        (fn [step]
                          [monochrome/monochrome-puzzle (:puzzle step)]))])]]))
