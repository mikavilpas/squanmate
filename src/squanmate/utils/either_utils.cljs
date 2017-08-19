(ns squanmate.utils.either-utils
  (:require [cats.monad.either :as either]
            [cats.core :as m]))

(defn when-right [e right-fn]
  (when (either/right? e)
    (right-fn (m/extract e))))
