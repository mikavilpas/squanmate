(ns squanmate.services.cubeshape-piece-swapper-test
  (:require [squanmate.services.cubeshape-piece-swapper :as sut]
            [cljs.test :as t :refer [is]]
            [squanmate.alg.puzzle :as p])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(defn- piece-id [p]
  (filterv some?
           [(-> p :colors :top)
            (-> p :colors :a)
            (-> p :colors :b)]))

(defn- piece-ids [pieces]
  (vec (map piece-id pieces)))

(defn- puzzle-id [puzzle]
  [(piece-ids (-> puzzle :top-layer :pieces))
   (piece-ids (-> puzzle :bottom-layer :pieces))])

;; Test dsl.
;; It gets unreadable if the pieces are repeated too much
(def default-top-pieces
  (-> p/square-square :top-layer :pieces piece-ids))

(def default-bottom-pieces
  (-> p/square-square :bottom-layer :pieces piece-ids))

(deftest swap-pieces-test []
  ;; A tip for making / debugging these tests:
  ;;
  ;; Use the command line test runner to get readable diffs on the exepcted and
  ;; actual results.

  (is (= (puzzle-id p/square-square)
         (puzzle-id
          (sut/swap-pieces p/square-square {})))
      "preserves order of pieces when no swaps are requested")

  (is (= [[[:top :front :left]
           [:top :right]
           [:top :left :back]
           [:top :back]
           [:top :back :right]
           [:top :left]
           [:top :right :front]
           [:top :front]]
          default-bottom-pieces]
         (puzzle-id
          (sut/swap-pieces p/square-square
                           {:ul-edge :ur-edge})))
      "swaps pieces on the top layer")

  (is (= [default-top-pieces
          [[:bottom :front]
           [:bottom :front :right]
           [:bottom :right]
           [:bottom :left :front]
           [:bottom :back]
           [:bottom :back :left]
           [:bottom :left]
           [:bottom :right :back]]]
         (puzzle-id
          (sut/swap-pieces p/square-square
                           {:dlf-corner :drb-corner})))
      "swaps pieces on the bottom layer")

  (is (= [[[:top :front :left]
           [:top :right]
           [:top :left :back]
           [:top :front]
           [:top :back :right]
           [:top :left]
           [:top :right :front]
           [:top :back]]
          default-bottom-pieces]
         (puzzle-id
          (sut/swap-pieces p/square-square
                           {
                            :ul-edge :ur-edge
                            :ub-edge :uf-edge})))
      "swaps two pairs of pieces on the top layer")

  (is (= [[[:top :front :left]
           [:bottom :left]
           [:top :left :back]
           [:top :back]
           [:top :back :right]
           [:top :right]
           [:top :right :front]
           [:top :front]]
          [[:bottom :front]
           [:bottom :front :right]
           [:bottom :right]
           [:bottom :right :back]
           [:bottom :back]
           [:bottom :back :left]
           [:top :left]
           [:bottom :left :front]]]
         (puzzle-id
          (sut/swap-pieces p/square-square
                           {:ul-edge :dl-edge})))
      "swaps pieces across layers"))
