(ns squanmate.ui.alg-display
  (:require [cats.monad.either :as either]
            [clojure.string :as str]
            [squanmate.alg.serialization :as serialization]
            [squanmate.services.alg-insights :as alg-insights]))

(defn- failed-scramble [error]
  [:div "scramble error:"
   [:div (str error)]])

(defn- insight-class [marker]
  (str "insight " (name marker)))

(defn- insight-classes [token]
  (str/join " " (map insight-class (:markers token))))

(defn- algorithm-step [token]
  [:span {:class (insight-classes token)}
   (serialization/step-to-str (:move token))])

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
