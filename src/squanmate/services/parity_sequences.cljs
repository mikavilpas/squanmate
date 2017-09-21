(ns squanmate.services.parity-sequences)

;; parity sequences are relative to OBRG

(def parity? #{"BOR"
               "BRG"
               "BGO"
               "GBR"
               "GOB"
               "GRO"
               "RBO"
               "RGB"
               "ROG"
               "ORB"
               "OGR"
               "OBG"})

(defn new-sequence []
  (take 3 (shuffle "OBRG")))
