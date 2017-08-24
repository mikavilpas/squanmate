(ns squanmate.ui.alg-importer-test
  (:require [squanmate.ui.alg-importer :as alg-importer]
            [squanmate.ui.alg-visualizer :as alg-visualizer]
            [clojure.test :as t :refer [is]]
            [cats.core :as m]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defonce ui-state (alg-visualizer/default-alg-visualizer-state))

(defcard-rg ui
  [alg-importer/ui ui-state])

(deftest starting-puzzle-for-alg-test []
  (is (= ["kite" "kite"]
         (-> "/"
             alg-importer/starting-puzzle-for-alg
             shapes/puzzle-layer-shape-names))
      "basic alg")

  (is (= ["kite" "square"]
         (-> "0,-1/1,0/-2,0/2,0/-2,0/1,0/3,0/"
             alg-importer/starting-puzzle-for-alg
             shapes/puzzle-layer-shape-names))
      "really long alg")

  (is (= ["mushroom" "square"]
         (-> "/-2/-3/"
             alg-importer/starting-puzzle-for-alg
             shapes/puzzle-layer-shape-names))
      "alg that ends in the position 1,-1")
  "algs that and in positions 1 or 0,-1 or similar are not supported")
