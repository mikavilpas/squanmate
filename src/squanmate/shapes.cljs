(ns squanmate.shapes
  (:require [squanmate.puzzle :as puzzle]
            [squanmate.ui.drawing.monochrome :as monochrome])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(def e puzzle/edge)
(def c puzzle/corner)

(def four-four [e c e e e, e c e e e])
(def five-three [e c e e e, e e c e e])
(def six-two [c e e e e, e e c e e])
(def seven-one [c e e e e, e e e c e])
(def eight [c e e e e e, e e e c])

(defn shape [name pieces]
  [:span name [monochrome/layer-component
               (puzzle/TopLayer. pieces)]])

(defcard-rg flowers
  [:div
   [:div "2 corners, 8 edges (5 shapes)"]
   [shape "4-4" four-four]
   [shape "5-3" five-three]
   [shape "6-2" six-two]
   [shape "7-1" seven-one]
   [shape "8" eight]])

(def two-two-two [e e c e e, c e e c])
(def three-three [e e e c c e e e c])
(def three-two-one [c e e e c e e c e])
(def three-one-two [c e e c e e e c e])
(def left-four-two [c e e e e c e e c])
(def right-four-two [c e e c e e e e c])
(def four-one-one [e c e e e e c e c])
(def left-five-one [c e e e e e c e c])
(def right-five-one [c e c e e e e e c])
(def six [c e e e e e e c c])

(defcard-rg three-corners
  [:div
   "3 corners, 6 edges (10 shapes)"
   [:div
    [shape "2-2-2" two-two-two]
    [shape "3-3" three-three]
    [shape "3-2-1" three-two-one]
    [shape "3-1-2" three-one-two]
    [shape "Left 4-2" left-four-two]
    [shape "Right 4-2" right-four-two]
    [shape "4-1-1" four-one-one]
    [shape "Left 5-1" left-five-one]
    [shape "Right 5-1" right-five-one]
    [shape "6" six]]])

(def kite [c e c e e c e c])
(def barrel [c e e c c e e c])
(def shield [e e c c c e e c])
(def left-fist [c e c e c e e c])
(def right-fist [c e e c e c e c])
(def left-pawn [c c e e e c e c])
(def right-pawn [c e c e e e c c])
(def mushroom [c c e e e c c e])
(def scallop [c c e e e e c c])

(defcard-rg four-corners
  [:div
   "4 corners, 4 edges (10 shapes)"
   [:div
    [shape "Kite" kite]
    [shape "Barrel" barrel]
    [shape "Shield" shield]
    [shape "Left fist" left-fist]
    [shape "Right fist" right-fist]
    [shape "Left pawn" left-pawn]
    [shape "Right pawn" right-pawn]
    [shape "Mushroom" mushroom]
    [shape "Scallop" scallop]]])

(def paired-edges [c c c c c e e])
(def perpendicular-edges [e c c c c e c])
(def parallel-edges [e c c c c e c])

(defcard-rg five-corners
  [:div
   "5 corners, 2 edges (3 shapes)"
   [:div
    [shape "Paired edges" paired-edges]
    [shape "Perpendicular edges" perpendicular-edges]
    [shape "Parallel edges" parallel-edges]]])

(def star [c c c c c c])

(defcard-rg six-corners
  [:div
   "6 corners, 0 edges (1 shape)"
   [:div
    [shape "Star" star]]])

(defcard-rg source
  [:div
   "The shape list and shape names were taken from "
   [:a {:href "http://www.cubezone.be/square1step1.html"
        :target "_blank"}
    "Lars Vandenbergh's CubeZone"]
   ". It is genious, and should be attributed to him."])
