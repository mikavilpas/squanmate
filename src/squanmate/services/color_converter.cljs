(ns squanmate.services.color-converter)

;; a color can be either a named color or an RGBA value like [r g b a]

(defn color->hex [color]
  (condp = color
    :white "#ffffff"
    :yellow "#fffe44"
    :blue "#625fff"
    :green "#47f543"
    :orange "#ffa743"
    :red "#ff4343"
    :gray "#aaaaaa"))

(def ^:private named-colors {:gray [169]
                             :white [253]
                             :yellow [255, 254, 69]

                             :orange [255, 166, 69]
                             :blue [99, 96, 255]
                             :red [255, 69, 69]
                             :green [69, 245, 69]})

(defn color-name->color [name-key]
  (get named-colors name-key))
