(ns squanmate.ui.alg-display
  (:require [cats.monad.either :as either]
            [clojure.string :as str]
            [squanmate.alg.serialization :as serialization]
            [squanmate.services.alg-insights :as alg-insights]
            [squanmate.services.alg-insights.types :as t]
            [squanmate.ui.common :as common]
            [reagent.core :as reagent]))

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

(defn- successful-scramble [tokens]
  (into [:div]
        (map algorithm-step tokens)))

(defn rich-scramble-display
  "Precondition: the `scramble-string` must start at square-square."
  [scramble-string]
  (either/branch
   (alg-insights/alg-with-cubeshape-status-highlighted scramble-string)
   failed-scramble
   successful-scramble))
