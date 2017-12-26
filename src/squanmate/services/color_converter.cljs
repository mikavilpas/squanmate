(ns squanmate.services.color-converter)

(defn color->hex [color]
  (condp = color
    :white "#ffffff"
    :yellow "#fffe44"
    :blue "#625fff"
    :green "#47f543"
    :orange "#ffa743"
    :red "#ff4343"
    :gray "#aaaaaa"))
