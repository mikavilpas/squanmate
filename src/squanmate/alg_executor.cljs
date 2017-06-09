(ns squanmate.alg-executor
  (:require [the.parsatron :as p]
            [cljs.reader :refer [read-string]])
  (:require-macros [the.parsatron :refer [let->> >> defparser]]))

;; here all algorithms are converted into steps,
;; which can then be executed later.

(defprotocol AlgorithmStep
  (execute [this puzzle]))

(defrecord Slice []
  AlgorithmStep
  (execute [this puzzle]))

(defrecord RotateTopLayer [amount]
  AlgorithmStep
  (execute [this puzzle]))

(defrecord RotateBottomLayer [amount]
  AlgorithmStep
  (execute [this puzzle]))

;;
;; tokens used in parsing

(defn optional [p]
  (p/either p (p/always nil)))

(defparser integer []
  (let->> [sign (optional (p/char "-"))
           digits (p/many1 (p/digit))]
    (p/always (read-string (str sign
                                (apply str digits))))))

(defparser slice []
  (p/>> (p/char "/")
        (p/always (Slice.))))

(defn whitespace? [character]
  (re-matches #"\s" character))

(defparser whitespace []
  (optional
   (p/choice (p/many (p/token whitespace?))
             ;; This character is used in some algorithms to show that
             ;; the algorithm flips the middle layer. It's currently
             ;; ignored by the parser.
             (p/many (p/char "*")))))

(defparser comma []
  (p/char ","))

(defparser rotation-instruction []
  (let->> [top-amount (integer)
           _ (whitespace)
           _ (comma)
           _ (whitespace)
           bottom-amount (integer)
           _ (whitespace)]
    (p/always [(RotateTopLayer. top-amount)
               (RotateBottomLayer. bottom-amount)])))

(defparser rotation-and-slice []
  (let->> [_ (whitespace)
           [top bottom] (rotation-instruction)
           s (slice)
           _ (whitespace)]
    (p/always [top bottom s])))

(defparser algorithm []
  (let->> [s (optional (slice))
           step-vectors (p/many (rotation-and-slice))]
    (let [steps (flatten step-vectors)]
      (if s
        (p/always (conj steps s))
        (p/always steps)))))

(defn parse
  "Supported formats:
  - / 1, -2 /
  - TODO / 1 / (allows leaving out bottom layer rotation)
  - TODO / (1, -2) /
  - TODO / UD' / U' / D /
  "
  [algorithm-string]
  (p/run (algorithm) algorithm-string))
