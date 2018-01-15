(ns squanmate.services.alg-insights.types)

(defprotocol InsightMarker
  (id [this])
  (description [this])
  (class-names [this]))

(defrecord Token [move markers])
