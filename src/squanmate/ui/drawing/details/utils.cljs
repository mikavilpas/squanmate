(ns squanmate.ui.drawing.details.utils
  (:require [quil.core :as q]))

(defn with-temporary-rotation [degrees function]
  (q/rotate (q/radians degrees))
  (function)
  (q/rotate (q/radians (- degrees))))
