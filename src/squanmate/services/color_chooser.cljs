(ns squanmate.services.color-chooser
  (:require [squanmate.services.color-settings :as color-settings]))

(defn make-monochrome [cs]
  (assoc cs
         :top :gray
         :bottom :gray
         ;; this is a bit hacky... but yolo LUL
         :monochrome? true))

(defn do-swap-top-and-bottom [cs]
  (assoc cs
         :top (:bottom cs)
         :bottom (:top cs)
         :left (:right cs)
         :right (:left cs)))

(defn turn-y2 [cs]
  (assoc cs
         :front (:back cs)
         :back (:front cs)
         :left (:right cs)
         :right (:left cs)))
