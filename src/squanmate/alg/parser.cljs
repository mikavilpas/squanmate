(ns squanmate.alg.parser
  (:require [the.parsatron :as p]
            [squanmate.alg.types :as types]
            [cljs.reader :refer [read-string]]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.utils.either-utils :as eu]
            [clojure.string :as str])
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

(defn whitespace? [character]
  (re-matches #"\s" character))

(defparser whitespace []
  (optional
   (p/choice (p/many (p/token whitespace?))
             ;; This character is used in some algorithms to show that
             ;; the algorithm flips the middle layer. It's currently
             ;; ignored by the parser.
             (p/many (p/char "*")))))

(defparser slice []
  (p/>> (whitespace)
        (p/char "/")
        (p/always (types/Slice.))))

(defparser in-parens-maybe [p]
  (let->> [_ (whitespace)
           _ (optional (p/char "("))
           result p
           _ (whitespace)
           _ (optional (p/char ")"))
           _ (whitespace)]
    (p/always result)))

(defparser comma []
  (p/char ","))

(defparser rotation-instruction-top-layer-only []
  (let->> [top-amount (integer)]
    (p/always (types/Rotations. top-amount 0))))

(defparser rotation-instruction []
  (let->> [_ (whitespace)
           top-amount (integer)
           _ (whitespace)
           _ (comma)
           _ (whitespace)
           bottom-amount (integer)]
    (p/always (types/Rotations. top-amount bottom-amount))))

(defn- non-nils [coll]
  (filterv (comp not nil?) coll))

(defparser rotations []
  (let->> [_ (whitespace)
           rotations (p/either (p/attempt (in-parens-maybe
                                           (rotation-instruction)))
                               (in-parens-maybe
                                (rotation-instruction-top-layer-only)))
           _ (whitespace)]
    (p/always rotations)))

(defparser empty-alg []
  (let->> [_ (whitespace)
           _ (p/eof)]
    (p/always [])))

(defparser step []
  (p/>> (whitespace)
        (p/either (p/attempt (slice))
                  (rotations))))

(defparser algorithm []
  (p/let->> [alg-steps (p/choice (p/attempt (p/many1 (step)))
                                 (p/attempt (empty-alg)))
             _ (whitespace)]
    (p/always alg-steps)))

(defn parse
  "Supported formats:
  - / 1, -2 /
  - / 1 / (allows leaving out bottom layer rotation)
  - / (1, -2) /
  "
  [algorithm-string]
  (let [result (either/try-either
                (p/run
                  (algorithm)
                  (str/trim algorithm-string)))]
    (m/left-map #(aget % "message") result)))
