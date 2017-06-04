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
  (p/either p (p/always "")))

(defparser integer []
  (let->> [sign (optional (p/char "-"))
           digits (p/many1 (p/digit))]
    (p/always (read-string (str sign
                                (apply str digits))))))

(defparser slice []
  (p/char "/"))

(defparser whitespace []
  (optional
   (p/choice (p/many (p/char " "))
             (p/many (p/char "\t"))
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

(defn parse
  "Supported formats:
  - TODO / 1, -2 /
  - TODO / 1 / (allows leaving out bottom layer rotation)
  - TODO / (1, -2) /
  - TODO / UD' / U' / D /
  "
  [algorithm-string]
  )
