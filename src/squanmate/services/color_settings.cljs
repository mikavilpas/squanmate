(ns squanmate.services.color-settings)

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
