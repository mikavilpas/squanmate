(ns squanmate.ui.alg-importer-test
  (:require [squanmate.ui.alg-importer :as alg-importer]
            [squanmate.ui.alg-visualizer :as alg-visualizer]
            [clojure.test :as t :refer [is]]
            [cats.core :as m]
            [squanmate.shapes :as shapes])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(defn- state-with-alg [alg-string]
  (let [a (alg-importer/default-alg-importer-state)]
    (swap! a assoc :algorithm alg-string)
    a))

(def ui-state (alg-importer/default-alg-importer-state))

(defcard-rg ui
  [alg-importer/ui ui-state])

(def ui-failed-state (state-with-alg "invalid algorithm"))
(defcard-rg ui-failed
  [alg-importer/ui ui-failed-state])

(def ui-successful-state (state-with-alg "/"))
(defcard-rg ui-successful
  [alg-importer/ui ui-successful-state])

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

(defn- import-alg [alg-string]
  (m/extract (alg-importer/import-alg alg-string)))

(deftest import-alg-test []
  (is (= {:reversed-alg "/ ",
          :starting-puzzle-spec
          {:top-name "kite",
           :bottom-name "kite",
           :initial-rotation {:top-amount 0, :bottom-amount 0}}}
         (import-alg "/"))
      "basic alg")

  (is (= {:reversed-alg
          "/ (-3,0)/ (-1,0)/ (2,0)/ (-2,0)/ (2,0)/ (-1,0)/ (0,1)",
          :starting-puzzle-spec
          {:top-name "kite",
           :bottom-name "square",
           :initial-rotation {:top-amount 3, :bottom-amount 1}}}

         ;; this is the odd kite square cubeshape parity alg
         (import-alg "0,-1/1,0/-2,0/2,0/-2,0/1,0/3,0/"))
      "really long alg that ends in the position 1,-1")

  (is (= {:reversed-alg "/ (3,0)/ (2,0)/ (6,0)",
          :starting-puzzle-spec
          {:top-name "mushroom",
           :bottom-name "square",
           :initial-rotation {:top-amount 6, :bottom-amount 0}}}
         (import-alg "6/-2/-3/"))
      "alg that has an initial rotation before the first slice")
  "algs that end in positions 1 or 0,-1 or similar are not supported")
