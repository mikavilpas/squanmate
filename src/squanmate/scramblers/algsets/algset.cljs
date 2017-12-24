(ns squanmate.scramblers.algsets.algset)

(defrecord AlgSet [odd-cases even-cases])

(defn all-cases [a]
  (into (:even-cases a)
        (:odd-cases a)))
