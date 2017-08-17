(ns squanmate.alg.serialization
  (:require [clojure.string :as str]
            [squanmate.alg.types :as types]
            [squanmate.alg.prettification :as prettification]))

(defn alg-to-str [steps]
  (str/join ""
            (for [s steps]
              (do
                (condp = (type s)
                  ;; carefully placing spaces only outside of (a,-b)/ groups
                  ;; makes the scramble to be rendered with very nice text
                  ;; wrapping in the browser :)
                  types/Slice "/ "
                  types/Rotations (str "(" (prettification/prettify-value
                                            (:top-amount s))
                                       ","
                                       (prettification/prettify-value
                                        (:bottom-amount s))
                                       ")"))))))
