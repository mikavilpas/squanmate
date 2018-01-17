(ns squanmate.scramblers.alg-trainer.algset-scrambler)

(defprotocol AlgSetScrambler
  "Defines how each algset generates a puzzle from a case. Allows for different
  kinds of algset scrambles to be used with the same interface."
  (generate-puzzle [this case]))

(defn all-cases [a]
  (into (:even-cases a)
        (:odd-cases a)))
