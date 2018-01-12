(ns squanmate.alg.serialization-test
  (:require [squanmate.alg.serialization :as serialization]
            [clojure.test :refer [is]]
            [squanmate.puzzle :as puzzle]
            [squanmate.alg.execution :as execution]
            [cats.core :as m]
            [squanmate.alg.rotation :as rotation]
            [squanmate.shapes :as shapes]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(def mushroom-rotated
  (let [s (m/extract (rotation/rotate-layer shapes/mushroom -5))]
    (puzzle/->TopLayer (:pieces s))))

(def settings (merge newmonochrome/default-settings
                     {:draw-mode {:monochrome? true}}))

(defcard-rg mushroom-rotated-preview
  [:div
   "This is used in a test below."
   [:div
    (let [m (:top-layer (shapes/puzzle-with-layers "mushroom" "mushroom"))]
      [:div
       "normal:"

       [newmonochrome/layer-component m settings]

       (puzzle/pieces-str m)])
    "rotated:" [newmonochrome/layer-component mushroom-rotated settings]
    (puzzle/pieces-str mushroom-rotated)]])

(deftest rotation-specification-test []
  (is (= ["mushroom" -5]
         (serialization/rotation-specification mushroom-rotated))))

(deftest puzzle-specification-test []
  (is (= {:top-name "square",
          :bottom-name "square",
          :initial-rotation "(0,1)"}
         (serialization/puzzle-specification puzzle/square-square))))
