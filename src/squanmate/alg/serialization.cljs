(ns squanmate.alg.serialization
  (:require [clojure.string :as str]
            [squanmate.alg.types :as types]
            [squanmate.alg.manipulation :as manipulation]))

(defn alg-to-str [steps]
  (str/join ""
            (for [s steps]
              (do
                (condp = (type s)
                  types/Slice "/"
                  types/Rotations (str "(" (manipulation/prettify-value
                                            (:top-amount s))
                                       ", "
                                       (manipulation/prettify-value
                                        (:bottom-amount s))
                                       ")"))))))
