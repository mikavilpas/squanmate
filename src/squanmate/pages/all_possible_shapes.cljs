(ns squanmate.pages.all-possible-shapes
  (:require [squanmate.puzzle :as puzzle]
            [devcards.core :as dc]      ; required
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.shapes :as s])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defn shape [shape]
  [:span (:name shape)
   [newmonochrome/layer-component
    (puzzle/TopLayer. (:pieces shape))
    :size 100]])

(defcard-rg introduction
  "This page contains a listing of all possible shapes a layer can have.")

(defcard-rg flowers
  [:div
   [:div "2 corners, 8 edges (5 shapes)"]
   [shape s/four-four]
   [shape s/five-three]
   [shape s/six-two]
   [shape s/seven-one]
   [shape s/eight]])

(defcard-rg three-corners
  [:div
   "3 corners, 6 edges (10 shapes)"
   [:div
    [shape s/two-two-two]
    [shape s/three-three]
    [shape s/three-two-one]
    [shape s/three-one-two]
    [shape s/left-four-two]
    [shape s/right-four-two]
    [shape s/four-one-one]
    [shape s/left-five-one]
    [shape s/right-five-one]
    [shape s/six]]])

(defcard-rg four-corners
  [:div
   "4 corners, 4 edges (11 shapes)"
   [:div
    [shape s/square]
    [shape s/kite]
    [shape s/barrel]
    [shape s/shield]
    [shape s/left-fist]
    [shape s/right-fist]
    [shape s/left-pawn]
    [shape s/right-pawn]
    [shape s/mushroom]
    [shape s/scallop]]])

(defcard-rg five-corners
  [:div
   "5 corners, 2 edges (3 shapes)"
   [:div
    [shape s/paired-edges]
    [shape s/perpendicular-edges]
    [shape s/parallel-edges]]])

(defcard-rg six-corners
  [:div
   "6 corners, 0 edges (1 shape)"
   [:div
    [shape s/star]]])

(defcard-rg source
  [:div
   "The shape list and shape names were taken from "
   [:a {:href "http://www.cubezone.be/square1step1.html"
        :target "_blank"}
    "Lars Vandenbergh's CubeZone"]
   ". It is genious, and should be attributed to him."])
