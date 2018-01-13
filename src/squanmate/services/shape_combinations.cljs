(ns squanmate.services.shape-combinations
  (:require [clojure.math.combinatorics :as combo]
            [squanmate.services.shapes :as shapes]))

(defn- pieces [piece-type a b]
  (let [all-pieces (into (:pieces a)
                         (:pieces b))]
    (filter (fn [p]
              (= piece-type
                 (:type p)))
            all-pieces)))

(defn- has-8-edges? [a b]
  (= 8 (count (pieces "e" a b))))

(defn- has-8-corners? [a b]
  (= 8 (count (pieces "c" a b))))

(defn- valid-shape-combination? [[a b]]
  (let [shape-a (get shapes/all-shapes a)
        shape-b (get shapes/all-shapes b)]
    (and (has-8-corners? shape-a shape-b)
         (has-8-edges? shape-a shape-b))))

(defonce possible-layers
  (let [shape-names (keys shapes/all-shapes)
        possible-combinations (combo/combinations
                               ;; Have all shape names twice, so combinations
                               ;; will find valid double shapes, like square
                               ;; square and kite kite and so on. This is really
                               ;; hacky but very fast, so who cares :)
                               (into shape-names
                                     shape-names)
                               2)]
    (filter valid-shape-combination? possible-combinations)))

(defn- uniquefy [things]
  (-> things set))

(defn- remove-same-shape-name [name [a b]]
  (if (= name a b)
    ;; This is a case where it's possible to have e.g. square square. So the
    ;; name is given twice, and must be returned too
    name
    ;; This is a case e.g. square kite, which means only the kite shape is
    ;; valuable to the caller
    (first (filter #(not (= name %)) [a b]))))

(defn filtered-possible-shapes [filter-shape-name]
  (->> possible-layers
       (filter (fn [shape-names]
                 (some #(= % filter-shape-name) shape-names)))
       (map #(remove-same-shape-name filter-shape-name %))
       flatten
       uniquefy))
