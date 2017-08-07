(ns squanmate.alg.serialization
  (:require [clojure.string :as str]
            [squanmate.alg.types :as types]))

(defn alg-to-str [steps]
  (str/join ""
            (for [s steps]
              (do
                (condp = (type s)
                  types/Slice "/"
                  types/Rotations (str "(" (:top-amount s)
                                       ", "
                                       (:bottom-amount s)
                                       ")"))))))
