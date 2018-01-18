(ns squanmate.scramblers.algsets.lin-corner-permutation
  "Each case can have even or odd parity, because only the order of corners and
  two edges is what defines each case."
  (:require [squanmate.scramblers.alg-trainer.algset-scrambler :as algset-scrambler]
            [squanmate.scramblers.alg-trainer.scramble-generators.partially-random-algset
             :as
             pra]
            [squanmate.services.cubeshape-piece-swapper :as swapper]))

(def ^:private cases-with-bottom-solved
  [["Diagonal corners (bottom solved)" "/ 3,3 / 1, -2 / 2, 2 / -3 / -3, -3 /"]
   ["Front corners (bottom solved)" "/ (3,-3) / (3,0) / (-3,0) / (0,3) / (-3,0) /"]])

(def ^:private cases-with-df-edge-unsolved
  [["Oriented corners" "(7, 6) / (3,0) / (3,0) / (-1,-1) / (-2,1) / (-3,0) /-1"]
   ["Opposite corners" "1,0 / -4,-3 / -3,0 / -3,-3 / -3,0 / -2,-3 /-1"]
   ["Left corners" "1,0 / 3,0 / 3,-3 / -1,2 / 1,-2 / 3,0 /-1"]
   ["Right corners" "1,0 / 2,-1 / 0,-3 / 3,0 / -3,0 / -2,4 /-1"]
   ["Front corners" "0,-1 / 4,-2 / -3,0 / 0,3 / 0,-3 / -1,2 /0,1"]
   ["Back corners" "4,-3 / -3,0 / -1,2 / 1,-2 / -3,3 / -3,0 /-1"]])

(def ^:private lin-top-edges-and-db-edge [;; don't scramble the dl edge because
                                          ;; the algs need it
                                          [:ub-edge :ur-edge :uf-edge]
                                          ;; it's okay to switch the two bottom
                                          ;; layer edges because that won't
                                          ;; affect the Lin CP case
                                          [:db-edge :ul-edge]])
(def ^:private lin-top-edges
  ;; this is used for cases where the bottom layer is solved
  [swapper/top-edges])

(defn- odd-parity-case? [[_name _alg {:keys [odd-case?]}]]
  (true? odd-case?))

(defn- belongs-to? [case-group case]
  (let [[name alg] case]
    ;; check membership without metadata, since metadata is only added for odd
    ;; parity cases
    (contains? (set case-group) [name alg])))

(defn- create-puzzle [case]
  (let [[name alg] case
        odd-case? (odd-parity-case? case)]
    (cond
      (belongs-to? cases-with-bottom-solved case)
      (pra/create-case-with-parity alg
                                   lin-top-edges
                                   odd-case?)

      (belongs-to? cases-with-df-edge-unsolved case)
      (pra/create-case-with-parity alg
                                   lin-top-edges-and-db-edge
                                   odd-case?)
      :else
      (throw (new js/Error (str "unknown Lin CP case " (pr-str case)))))))

(defrecord LinCornerPermutationAlgSet [odd-cases even-cases]
  algset-scrambler/AlgSetScrambler
  (generate-puzzle [this case]
    (create-puzzle case)))

(defn- parity-versionize [[name alg]]
  [name alg {:odd-case? true}])

(def ^:private all-cases (into cases-with-df-edge-unsolved
                               cases-with-bottom-solved))

(def lin-cp-algset
  (->LinCornerPermutationAlgSet (map parity-versionize all-cases)
                                all-cases))
