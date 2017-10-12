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

(defn- face-move-letter-to-rotations [move reverse?]
  (letfn [(convert [amount]
            (if reverse?
              (- amount)
              amount))]
    (condp = move
      "U" (types/->Rotations (convert 3) 0)
      "D" (types/->Rotations 0 (convert 3))
      "U2" (types/->Rotations 6 0)
      "D2" (types/->Rotations 0 6))))

(defparser face-notation-move []
  (let->> [_ (whitespace)
           move (p/choice (p/attempt (p/string "U2"))
                          (p/attempt (p/string "D2"))
                          (p/char "U")
                          (p/char "D"))
           reverse? (optional (p/char "'"))
           _ (whitespace)]
    (let [rotations (face-move-letter-to-rotations move reverse?)]
      (p/always rotations))))

(def m2 [(types/->Rotations 1 0)
         (types/Slice.)
         (types/->Rotations -1 -1)
         (types/Slice.)
         (types/->Rotations 0 1)])

(defparser m2-algorithm []
  (let->> [_ (whitespace)
           _ (p/string "M2")]
    (p/always m2)))

(defparser rotations []
  (let->> [_ (whitespace)
           rotations (p/choice (p/attempt (in-parens-maybe (rotation-instruction)))
                               (p/attempt (face-notation-move))
                               (p/attempt (m2-algorithm))
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
                                 (empty-alg))
             _ (whitespace)]
    (p/always (flatten alg-steps))))

(defn parse
  "Supported formats:
  - / 1, -2 /
  - / 1 / (allows leaving out bottom layer rotation)
  - / (1, -2) /
  - U / D' / U2' / D2 /
  - U2 / M2 / U2 / M2
      in this case M2 means the alg 1,0/-1,-1/0,1
  "
  [algorithm-string]
  (let [result (either/try-either
                (p/run
                  (algorithm)
                  (str/trim algorithm-string)))]
    (m/left-map #(aget % "message") result)))
