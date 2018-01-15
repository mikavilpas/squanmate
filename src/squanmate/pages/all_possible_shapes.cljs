(ns squanmate.pages.all-possible-shapes
  (:require [squanmate.services.shapes :as s]
            [squanmate.ui.common :as common]
            [squanmate.ui.shape-preview :as sp]
            [squanmate.pages.page-content :as page-content]))

(defn- flowers []
  [:div
   [:div "2 corners, 8 edges (5 shapes)"]
   [sp/shape-preview s/four-four]
   [sp/shape-preview s/five-three]
   [sp/shape-preview s/six-two]
   [sp/shape-preview s/seven-one]
   [sp/shape-preview s/eight]])

(defn- three-corners []
  [:div
   "3 corners, 6 edges (10 shapes)"
   [:div
    [sp/shape-preview s/two-two-two]
    [sp/shape-preview s/three-three]
    [sp/shape-preview s/three-two-one]
    [sp/shape-preview s/three-one-two]
    [sp/shape-preview s/left-four-two]
    [sp/shape-preview s/right-four-two]
    [sp/shape-preview s/four-one-one]
    [sp/shape-preview s/left-five-one]
    [sp/shape-preview s/right-five-one]
    [sp/shape-preview s/six]]])

(defn- four-corners []
  [:div
   "4 corners, 4 edges (10 shapes)"
   [:div
    [sp/shape-preview s/square]
    [sp/shape-preview s/kite]
    [sp/shape-preview s/barrel]
    [sp/shape-preview s/shield]
    [sp/shape-preview s/left-fist]
    [sp/shape-preview s/right-fist]
    [sp/shape-preview s/left-pawn]
    [sp/shape-preview s/right-pawn]
    [sp/shape-preview s/mushroom]
    [sp/shape-preview s/scallop]]])

(defn- five-corners []
  [:div
   "5 corners, 2 edges (3 shapes)"
   [:div
    [sp/shape-preview s/paired-edges]
    [sp/shape-preview s/perpendicular-edges]
    [sp/shape-preview s/parallel-edges]]])

(defn- six-corners []
  [:div
   "6 corners, 0 edges (1 shape)"
   [:div
    [sp/shape-preview s/star]]])

(defn- source []
  [:div
   "The shape list and shape names were taken from "
   [:a {:href "http://www.cubezone.be/square1step1.html"
        :target "_blank"}
    "Lars Vandenbergh's CubeZone"]
   ". It is genious, and should be attributed to him."])

(defn content []
  [:div.row.col-xs-12
   "This page contains a listing of all possible shapes a layer can have."
   [common/panel [flowers]]
   [common/panel [three-corners]]
   [common/panel [four-corners]]
   [common/panel [five-corners]]
   [common/panel [six-corners]]
   [source]])

(defmethod page-content/page :shapes []
  [content])
