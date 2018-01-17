(ns squanmate.scramblers.algsets.cubeshape
  (:require [squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset :as pra]
            [squanmate.services.cubeshape-piece-swapper :as swapper]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]))

;; only one case, and no specific alg to solve

(def ^:private odd-cases
  [["Cubeshape (odd parity)" "0" {:odd-case? true}]])

(def ^:private even-cases
  [["Cubeshape" "0" {:odd-case? false}]])

(defn- odd-parity-case? [[_name _alg {:keys [odd-case?]}]]
  (true? odd-case?))

(def all-pieces [swapper/piece-ids])

(defn- create-puzzle [[name alg :as case]]
  (let [want-odd-parity? (odd-parity-case? case)]
    (pra/create-case-with-parity alg all-pieces want-odd-parity?)))

(defrecord CubeshapeAlgSet [odd-cases even-cases]
  algset-scrambler/AlgSetScrambler
  (generate-puzzle [this case]
    (create-puzzle case)))

(def cubeshape-algset
  (->CubeshapeAlgSet odd-cases even-cases))
