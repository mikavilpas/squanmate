(ns squanmate.alg.parity-counter
  (:require [squanmate.puzzle :as p]
            [squanmate.alg.slicing :as slicing]
            [clojure.string :as str]
            [squanmate.services.shapes :as shapes]))

(defrecord ParityCount[top-corner-order
                       top-edge-order
                       bottom-corner-order
                       bottom-edge-order
                       top-edges-in-odd-edge-positions
                       top-corners-in-odd-corner-positions])

(def ^:private parity-side-sequences #{[:left :front :back]
                                       [:left :back :right]
                                       [:left :right :front]
                                       [:right :left :back]
                                       [:right :front :left]
                                       [:right :back :front]
                                       [:back :left :front]
                                       [:back :right :left]
                                       [:back :front :right]
                                       [:front :back :left]
                                       [:front :right :back]
                                       [:front :left :right]})

(defn- top-corner? [piece]
  (and (p/corner? piece)
       (p/top-piece? piece)))

(defn- top-edge? [piece]
  (and (p/edge? piece)
       (p/top-piece? piece)))

(defn- bottom-corner? [piece]
  (and (p/corner? piece)
       (p/bottom-piece? piece)))

(defn- bottom-edge? [piece]
  (and (p/edge? piece)
       (p/bottom-piece? piece)))

(defn- first-clockwise-side [piece]
  (-> piece :colors :a))

(defn- parity-for-pieces [pieces]
  (let [three-pieces (take 3 pieces)
        clockwise-sides (vec (map first-clockwise-side three-pieces))]
    (if (parity-side-sequences clockwise-sides)
      {:parity-count 1, :pieces three-pieces}
      {:parity-count 0, :pieces three-pieces})))

(defn- top-corner-order-parity [pieces]
  (parity-for-pieces (filter top-corner? pieces)))

(defn- top-edge-order-parity [pieces]
  (parity-for-pieces (filter top-edge? pieces)))

(defn- bottom-corner-order-parity [pieces]
  (parity-for-pieces (filter bottom-corner? pieces)))

(defn- bottom-edge-order-parity [pieces]
  (parity-for-pieces (filter bottom-edge? pieces)))

(defn- debug-pieces [pieces]
  (str/join ", "
            (for [p pieces]
              (str (:type p) " "
                   (-> p :colors :top)))))

(defn pieces-in-count-order [puzzle]
  (let [[top-left top-right] (slicing/split-at-6 (:top-layer puzzle))
        [bottom-left bottom-right] (slicing/split-at-6 (:bottom-layer puzzle))
        result-pieces (vec (flatten [top-right
                                     top-left
                                     bottom-left
                                     bottom-right]))]
    result-pieces))

(defprotocol PiecesInOddPositions
  (corner-or-edge? [this piece])
  (correct-top-side? [this piece]))

(def top-odd-corners
  (reify PiecesInOddPositions
    (corner-or-edge? [this piece]
      (p/corner? piece))
    (correct-top-side? [this piece]
      (top-corner? piece))))

(def top-odd-edges
  (reify PiecesInOddPositions
    (corner-or-edge? [this piece]
      (p/edge? piece))
    (correct-top-side? [this piece]
      (top-edge? piece))))

(defn- pieces-in-odd-piece-positions [strategy pieces]
  (let [indices-and-pieces (zipmap (range 1 100)
                                   (filter #(corner-or-edge? strategy %) pieces))
        odd-positioned-pieces (filter (fn [[index piece]]
                                        (and (odd? index)
                                             (correct-top-side? strategy piece)))
                                      indices-and-pieces)]
    {:parity-count (count odd-positioned-pieces)
     :pieces odd-positioned-pieces}))

(defn parity-count
  "Note: the puzzle has to be sliceable at the current position!"
  [puzzle]
  (let [pieces (pieces-in-count-order puzzle)
        pc (map->ParityCount
            {:top-corner-order (top-corner-order-parity pieces)
             :top-edge-order (top-edge-order-parity pieces)
             :bottom-corner-order (bottom-corner-order-parity pieces)
             :bottom-edge-order (bottom-edge-order-parity pieces)
             :top-edges-in-odd-edge-positions (pieces-in-odd-piece-positions top-odd-edges pieces)
             :top-corners-in-odd-corner-positions (pieces-in-odd-piece-positions top-odd-corners pieces)})
        parity-counts (map :parity-count (vals pc))

        at-odd-parity? (= 1 (-> (apply + parity-counts)
                                (mod 2)))]
    [at-odd-parity? pc]))
