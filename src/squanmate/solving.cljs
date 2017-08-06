(ns squanmate.solving
  (:require [clojure.string :as str]))

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
    (println p piece-id)
    (get conversions
         piece-id
         (str "oops! piece " (pr-str p) " could not be converted!"))))

(defn convert-to-state-string [puzzle]
  (let [pieces (into (-> puzzle :top-layer :pieces)
                     (-> puzzle :bottom-layer :pieces))]
    (apply str
           (mapv convert-piece pieces))))
