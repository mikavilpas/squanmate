(ns squanmate.utils.either-utils
  (:require [cats.monad.either :as either]
            [cats.core :as m]))

(defn when-right [e right-fn]
  (when (either/right? e)
    (right-fn (m/extract e))))

(defn list-of-eithers->either-list
  "If the given `list-of-eithers` is all rights, returns their values in a
  sequence, like (right [1 2 3]). Otherwise returns the last left value.

  Does not short circuit!"
  ;; currently no use case for short circuiting
  [list-of-eithers]
  (let [initial-value (either/right [])]
    (reduce (fn [xs-either a-either]
              (m/mlet [a a-either
                       xs xs-either]
                      (m/return (conj xs a))))
            initial-value
            list-of-eithers)))
