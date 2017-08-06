(ns squanmate.solving
  (:require [clojure.string :as str]
            [squanmate.alg.execution :as execution]
            [cats.monad.either :as either]))

(defn- new-solver []
  ;; api:
  ;; new Worker("js/solver-worker.js").proxy()("solve")("start_state_encoded", function(err,result){[]});
  (js* "new Worker('js/solver-worker.js').proxy()('solve')"))

(defn solve [starting-state-string]
  (let [result-atom (atom nil)
        solver (new-solver)]
    (solver starting-state-string
            (fn callback [err, result]
              (when err
                (reset! result-atom (str "failed: " err)))
              (when result
                (reset! result-atom result))))
    result-atom))

(def conversions {[:top :front :left] "A"
                  [:top :left] "1"
                  [:top :left :back] "B"
                  [:top :back] "2"
                  [:top :back :right] "C"
                  [:top :right] "3"
                  [:top :right :front] "D"
                  [:top :front] "4"

                  [:bottom :front] "5"
                  [:bottom :front :right] "E"
                  [:bottom :right] "6"
                  [:bottom :right :back] "F"
                  [:bottom :back] "7"
                  [:bottom :back :left] "G"
                  [:bottom :left] "8"
                  [:bottom :left :front] "H"})

(defn- convert-piece [p]
  (let [piece-id (filterv some?
                          [(-> p :colors :top)
                           (-> p :colors :a)
                           (-> p :colors :b)])]
    (get conversions
         piece-id
         (str "oops! piece " (pr-str p) " could not be converted!"))))

(defn- bottom-layer-pieces [puzzle]
  ;; this is a naive approach. if there are some problems with this, this will
  ;; need to be revised.
  (let [p1 (execution/transformation-result puzzle "0,-6")
        p2 (execution/transformation-result puzzle "0,-5")
        turned-puzzle (first (either/rights [p1 p2]))]
    (either/branch
     turned-puzzle
     (fn [& _]
       (throw (new js/Error "the puzzle could not be converted to a state string for solving it.")))
     (fn [result]
       (js-debugger)
       (-> result :puzzle :bottom-layer :pieces)))))

(defn convert-to-state-string [puzzle]
  (let [pieces (into (-> puzzle :top-layer :pieces)
                     (bottom-layer-pieces puzzle))]
    (apply str
           (mapv convert-piece pieces))))
