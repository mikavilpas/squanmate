(ns squanmate.ui.shape-preview
  (:require [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [squanmate.puzzle :as puzzle]
            [squanmate.shapes :as shapes]))

(defn shape
  ([s]
   (shape {}))

  ([shape {:keys [size]}]
   [common/well {:style {:display "inline-block"}
                 :bs-size "small"}
    [:div [newmonochrome/layer-component
           (puzzle/TopLayer. (:pieces shape))
           {:size size}]]
    [:div.center (:name shape)]]))

(defn shape-preview-for-shape-name [name settings]
  (let [s (get shapes/all-shapes name)]
    [shape s settings]))
