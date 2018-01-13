(ns squanmate.ui.shape-preview
  (:require [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.ui.common :as common]
            [squanmate.puzzle :as puzzle]
            [squanmate.services.shapes :as shapes]))

(defn shape-preview [shape {:keys [size]}]
  [common/well {:style {:display "inline-block"}
                :bs-size "small"}
   [:div.center [newmonochrome/layer-component
                 (puzzle/TopLayer. (:pieces shape))
                 (merge newmonochrome/default-settings
                        {:size size
                         :draw-mode {:monochrome? true}})]]
   [:div.center (:name shape)]])
