(ns squanmate.scramblers.algsets.cubeshape
  (:require [squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset :as pra]
            [squanmate.services.cubeshape-piece-swapper :as swapper]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]))

;; only one case, and no specific alg to solve

(def ^:private empty-alg "")

(def ^:private odd-cases
  [["Cubeshape (odd parity)" empty-alg {:odd-case? true}]])

(def ^:private even-cases
  [["Cubeshape" empty-alg {:odd-case? false}]])

(defn- odd-parity-case? [[_name _alg {:keys [odd-case?]}]]
  (true? odd-case?))

(defn- create-puzzle [[name alg :as case]]
  (let [want-odd-parity? (odd-parity-case? case)]
    (pra/create-case-with-parity alg
                                 [swapper/edges
                                  swapper/corners]
                                 want-odd-parity?)))

(defrecord CubeshapeAlgSet [odd-cases even-cases]
  algset-scrambler/AlgSetScrambler
  (generate-puzzle [this case]
    (create-puzzle case)))

(def cubeshape-algset
  (->CubeshapeAlgSet odd-cases even-cases))
