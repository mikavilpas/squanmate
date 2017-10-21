(ns squanmate.ui.drawing.color-settings)

(defrecord ColorSettings [top bottom
                          left right
                          front back])

(def defaults (map->ColorSettings
               {:top :white
                :bottom :yellow
                :left :blue
                :right :green
                :front :orange
                :back :red}))

(defn make-monochrome [cs]
  (assoc cs
         :top :gray
         :bottom :gray
         ;; this is a bit hacky... but yolo LUL
         :monochrome? true))

(defn swap-top-and-bottom [cs]
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
