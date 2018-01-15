(ns squanmate.services.alg-insights.alignment
  (:require [squanmate.services.alg-insights.cubeshape :as cubeshape]
            [squanmate.services.shapes :as shapes]
            [squanmate.services.cube-aligner :as cube-aligner]))

(defn- in-cubeshape? [step]
  (= ["square" "square"]
     (:layer-names step)))

(defrecord LeavingCubeshape [aligned?])
(defrecord EnteredCubeshape [aligned?])

(defn- leaving-or-entered-cubeshape? [[a b]]
  (or (in-cubeshape? a)
      (in-cubeshape? b)))

(defn- add-layer-names [step]
  (let [puzzle (:puzzle step)
        names (shapes/puzzle-layer-shape-names puzzle)]
    ;; :index will be set later. It's here only so that the map is easier to
    ;; understand.
    {:layer-names names
     :step step
     :index nil}))

(defn- add-indices [steps]
  (map (fn [index step]
         (assoc step :index index))
       (range)
       steps))

(defn- get-cubeshape-entering-or-leaving-steps [steps]
  (let [successive-step-pairs (partition 2 1 steps)]
    (filter leaving-or-entered-cubeshape? successive-step-pairs)))

(defn- leaving-cubeshape? [[step-a step-b]]
  (and (in-cubeshape? step-a)
       (not (in-cubeshape? step-b))))

(def ^:private entered-cubeshape?
  (complement leaving-cubeshape?))

(defn- aligned? [step]
  (let [puzzle (-> step :step :puzzle)]
    (cube-aligner/aligned? puzzle)))

(defn- convert-to-marker [step-pair]
  ;; precondition: exactly one item in step-pair must be at cubeshape
  (let [[a b] step-pair]
    ;; Get the alignment of the step that is in cubeshape.
    ;; Notice this step is either the first or second step each time.
    ;; Notice also that the index will change depending on the case.
    (cond
      (leaving-cubeshape? step-pair)
      [(:index a),
       (->LeavingCubeshape (aligned? a))]

      (entered-cubeshape? step-pair)
      [(:index b),
       (->EnteredCubeshape (aligned? b))])))

(defn alignments-when-entering-or-leaving-cubeshape
  "There are two ways of entering or leaving cubeshape (square square):
  - at (0,0) (the aligned cubeshape, aka the solved puzzle)
  - at (1,-1)

  The only shape to reach square square is kite kite.

  This returns the alignment of steps that either:
  - start at kite kite, and go into square square
  - start at square square, and go into kite kite
  "
  [alg-steps]
  (->> alg-steps
       (map add-layer-names)
       (add-indices)
       (get-cubeshape-entering-or-leaving-steps)
       (map convert-to-marker)
       (into {})))
