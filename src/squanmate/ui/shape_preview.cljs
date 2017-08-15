(ns squanmate.ui.shape-preview
  (:require [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [squanmate.puzzle :as puzzle]
            [squanmate.shapes :as shapes]))

(defn shape-preview [shape {:keys [size]}]
  [common/well {:style {:display "inline-block"}
                :bs-size "small"}
   [:div.center [newmonochrome/layer-component
                 (puzzle/TopLayer. (:pieces shape))
                 {:size size}]]
   [:div.center (:name shape)]])
