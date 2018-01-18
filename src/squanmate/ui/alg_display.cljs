(ns squanmate.ui.alg-display
  (:require [cats.monad.either :as either]
            [clojure.string :as str]
            [squanmate.alg.serialization :as serialization]
            [squanmate.services.alg-insights :as alg-insights]
            [squanmate.services.alg-insights.types :as t]
            [squanmate.ui.common :as common]
            [reagent.core :as reagent]
            [squanmate.alg.types :as types]))

(defn- failed-scramble [error]
  [:div "scramble error:"
   [:div (str error)]])

(defn- insight-class [marker]
  (str "insight "
       (str/join " " (t/class-names marker))))

(defn- insight-classes [token]
  (str/join " " (map insight-class (:markers token))))

(defn- insight-description [marker]
  (t/description marker))

(defn- marker-descriptions [markers]
  (into [:div]
        (for [m markers]
          [:div (insight-description m)])))

(defn- algorithm-step [token]
  (let [classes-string (insight-classes token)
        description (marker-descriptions (:markers token))]
    [common/overlay-trigger
     {:overlay (reagent/as-element [common/tooltip {:id (str token description)}
                                    description])
      :placement "top"}
     [:span {:class classes-string}
      (serialization/step-to-str (:move token))]]))

(defn- bundle-rotations-and-slice-pairs
  "Group rotations and slices together, so they can be wrapped nicely in the UI."
  [tokens]
  (loop [token-groups []
         tokens tokens]
    (let [[a b] (take 2 tokens)]
      (js/console.log (:move a) (:move b))
      (cond
        (nil? a)
        token-groups

        (and (= types/Rotations (type (:move a)))
             (= types/Slice (type (:move b))))
        (recur (conj token-groups [a b])
               (drop 2 tokens))

        ;; This case means: either a is the last item, or a is a Slice and b is
        ;; a Rotations type (so they are in the wrong order).
        :else
        (recur (conj token-groups [a])
               (next tokens))))))

(defn- algorithm-step-group [group-of-tokens]
  (into [:span.alg-step-group]
        (map algorithm-step group-of-tokens)))

(defn- successful-scramble [tokens]
  [:div
   (let [groups (bundle-rotations-and-slice-pairs tokens)]
     (into [:div]
           (map algorithm-step-group groups)))])

(defn rich-scramble-display
  "Precondition: the `scramble-string` must start at square-square."
  [scramble-string]
  (either/branch
   (alg-insights/alg-with-cubeshape-status-highlighted scramble-string)
   failed-scramble
   successful-scramble))
