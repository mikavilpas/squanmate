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

(defn- puzzle-names-for-alg [alg-string]
  (-> alg-string
      alg-importer/starting-puzzle-for-alg
      m/extract
      :puzzle
      shapes/puzzle-layer-shape-names))

(deftest starting-puzzle-for-alg-test []
  (is (= ["kite" "kite"]
         (puzzle-names-for-alg "/"))
      "basic alg")

  (is (= ["kite" "square"]
         (puzzle-names-for-alg "0,-1/1,0/-2,0/2,0/-2,0/1,0/3,0/"))
      "really long alg")

  (is (= ["mushroom" "square"]
         (puzzle-names-for-alg "/-2/-3/"))
      "alg that ends in the position 1,-1")
  "algs that end in positions 1 or 0,-1 or similar are not supported")
