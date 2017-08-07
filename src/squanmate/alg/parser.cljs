(ns squanmate.alg.parser
  (:require [the.parsatron :as p]
            [squanmate.alg.types :as types]
            [cljs.reader :refer [read-string]]
            [cats.monad.either :as either])
  (:require-macros [the.parsatron :refer [let->> >> defparser]]
                   [cats.monad.either :refer [try-either]]))

;; here all algorithms are converted into steps,
;; which can then be executed later.

(defn optional [p]
  (p/either p (p/always nil)))

(defparser integer []
  (let->> [sign (optional (p/char "-"))
           digits (p/many1 (p/digit))]
    (p/always (read-string (str sign
                                (apply str digits))))))

(defparser slice []
  (p/>> (p/char "/")
        (p/always (types/Slice.))))

(defn whitespace? [character]
  (re-matches #"\s" character))

(defparser whitespace []
  (optional
   (p/choice (p/many (p/token whitespace?))
             ;; This character is used in some algorithms to show that
             ;; the algorithm flips the middle layer. It's currently
             ;; ignored by the parser.
             (p/many (p/char "*")))))

(defparser in-parens-maybe [p]
  (let->> [_ (whitespace)
           _ (optional (p/char "("))
           _ (whitespace)
           result p
           _ (whitespace)
           _ (optional (p/char ")"))
           _ (whitespace)]
    (p/always result)))

(defparser comma []
  (p/char ","))

(defparser rotation-instruction-top-layer-only []
  (let->> [top-amount (integer)
           _ (whitespace)]
    (p/always [(types/Rotations. top-amount 0)])))

(defparser rotation-instruction []
  (let->> [top-amount (integer)
           _ (whitespace)
           _ (comma)
           _ (whitespace)
           bottom-amount (integer)
           _ (whitespace)]
    (p/always [(types/Rotations. top-amount bottom-amount)])))

(defn- non-nils [coll]
  (filterv (comp not nil?) coll))

(defparser rotation-and-slice []
  (let->> [_ (whitespace)
           rotations (p/either (p/attempt (in-parens-maybe
                                           (rotation-instruction)))
                               (in-parens-maybe
                                (rotation-instruction-top-layer-only)))
           s (optional (slice))
           _ (whitespace)]
    (let [steps (conj rotations s)]
      (p/always (non-nils steps)))))

(defparser algorithm []
  (let->> [s (optional (slice))
           step-vectors (p/many (rotation-and-slice))]
    (let [steps (flatten step-vectors)]
      (p/always (non-nils (conj steps s))))))

(defn parse
  "Supported formats:
  - / 1, -2 /
  - / 1 / (allows leaving out bottom layer rotation)
  - / (1, -2) /
  "
  [algorithm-string]
  (either/try-either
   (p/run (algorithm) algorithm-string)))
