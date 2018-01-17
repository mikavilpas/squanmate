(ns squanmate.scramblers.algsets.lin-corner-permutation
  "Each case can have even or odd parity, because only the order of corners and
  two edges is what defines each case."
  (:require [squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset :as partially-random-algset]
            [squanmate.services.cubeshape-piece-swapper :as swapper]
            [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]))

(def ^:private cases-with-df-edge-solved
  (sorted-set
   ["Diagonal corners (bottom solved)" "/ 3,3 / 1, -2 / 2, 2 / -3 / -3, -3 /"]))

(def ^:private cases-with-df-edge-unsolved
  (sorted-set
   ["Oriented corners" "(7, 6) / (3,0) / (3,0) / (-1,-1) / (-2,1) / (-3,0) /-1"]
   ["Opposite corners" "1,0 / -4,-3 / -3,0 / -3,-3 / -3,0 / -2,-3 /-1"]
   ["Left corners" "1,0 / 3,0 / 3,-3 / -1,2 / 1,-2 / 3,0 /-1"]
   ["Right corners" "1,0 / 2,-1 / 0,-3 / 3,0 / -3,0 / -2,4 /-1"]
   ["Front corners" "0,-1 / 4,-2 / -3,0 / 0,3 / 0,-3 / -1,2 /0,1"]
   ["Back corners" "4,-3 / -3,0 / -1,2 / 1,-2 / -3,3 / -3,0 /-1"]))

(def ^:private lin-top-edges [swapper/top-edges])
(def ^:private lin-top-edges-and-df-edge [[:ub-edge :ur-edge :uf-edge]
                                          [:db-edge :ul-edge]])

(defn- create-puzzle [case]
  (let [[name alg] case]
    (cond
      (contains? cases-with-df-edge-solved case)
      (partially-random-algset/create-puzzle alg lin-top-edges)

      (contains? cases-with-df-edge-unsolved case)
      (partially-random-algset/create-puzzle alg lin-top-edges-and-df-edge))))

(defrecord LinCornerPermutationAlgSet [odd-cases even-cases]
  algset-scrambler/AlgSetScrambler
  (generate-puzzle [this case]
    (create-puzzle case)))

(def lin-cp-algset
  (->LinCornerPermutationAlgSet []
                                (into cases-with-df-edge-unsolved
                                      cases-with-df-edge-solved)))
