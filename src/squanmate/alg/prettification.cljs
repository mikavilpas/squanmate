(ns squanmate.alg.prettification)

;; this is kind of lame, but at least it's super readable
(def ^:private prettifications {7 -5
                                8 -4
                                9 -3
                                10 -2
                                11 -1
                                12 0

                                -6 6
                                -7 5
                                -8 4
                                -9 3
                                -10 2
                                -11 1
                                -12 0})

(defn prettify-value [n]
  (let [not-found n]
    (get prettifications n not-found)))
