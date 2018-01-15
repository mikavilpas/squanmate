(ns squanmate.services.alg-insights.types)

(defprotocol InsightMarker
  (id [this])
  (description [this]))

(defrecord Token [move markers])
