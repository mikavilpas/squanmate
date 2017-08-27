(ns squanmate.ui.alg-importer
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [reagent.core :as reagent]
            [squanmate.alg.manipulation :as manipulation]
            [squanmate.ui.common :as common]
            [squanmate.ui.parity :as parity]
            [squanmate.alg.serialization :as serialization]
            [squanmate.shapes :as shapes]
            [clojure.string :as str]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [squanmate.alg.execution :as execution]
            [squanmate.rotation :as rotation]
            [squanmate.pages.links :as links]))

(defn default-alg-importer-state []
  (reagent/atom {:algorithm nil}))

(defn starting-puzzle-for-alg [alg-string]
  (m/mlet [transformation-steps (parity/cubeshape-start-&-end-positions alg-string)]
          (m/return (-> transformation-steps last))))

(defn import-alg [alg-string]
  (m/mlet [start-transformation-step (starting-puzzle-for-alg alg-string)]
          (let [puzzle-spec (-> start-transformation-step
                                :puzzle
                                serialization/puzzle-specification)
                reversed-alg (manipulation/reverse-alg alg-string)]
            (m/return {:reversed-alg reversed-alg
                       :starting-puzzle-spec puzzle-spec}))))

(defn puzzle-from-spec [spec]
  (let [top (-> spec :starting-puzzle-spec :top-name)
        bottom (-> spec :starting-puzzle-spec :bottom-name)
        p (shapes/puzzle-with-layers top bottom)
        rotation (-> spec :starting-puzzle-spec :initial-rotation) ]
    (m/mlet [result (execution/transformation-result p rotation)]
            (m/return (:puzzle result)))))

(defn- error-box [error]
  [common/alert {:bs-style :warning}
   [:div [:strong "Error: "] error]])

(defn- import-button [spec]
  [common/button {:bs-style :success
                  :on-click #(links/set-link-to-visualization
                              (let [puzzle-spec (:starting-puzzle-spec spec)]
                                {:top-name (:top-name puzzle-spec)
                                 :bottom-name (:bottom-name puzzle-spec)
                                 :initial-rotation (:initial-rotation puzzle-spec)
                                 :algorithm (:reversed-alg spec)}))}
   "Import to Algorithm shape visualizer"])

(defn- success-box [spec]
  (let [p (puzzle-from-spec spec)]
    (either/branch
     p
     (fn [error]
       (println "Internal error: could not render starting step: " (pr-str error))
       [:div "Internal error. Please report this as an issue! " (pr-str error)])
     (fn [starting-puzzle]
       [common/alert {:bs-style :success}
        [:strong "Success!"]
        [:div "Looks like the algorithm starts at this state:"]
        [newmonochrome/monochrome-puzzle starting-puzzle]
        [import-button spec]]))))

(defn- import-alg-component [state]
  (println "import-alg-component with " state)
  (let [alg-string (:algorithm @state)]
    (when-not (str/blank? alg-string)
      (let [result (import-alg alg-string)]
        [:div
         (either/branch result
                        error-box
                        success-box)]))))

(defn ui [state]
  [:div
   [:h2 "Instructions:"]
   "Use this if you want to inspect an algorithm with Squanmate."
   [:div
    "Enter an algorithm that ends in cubeshape. Acceptable ending positions are (0) or (1,-1)."]
   [common/input-box (reagent/cursor state [:algorithm]) "Cubeshape algorithm"]
   [import-alg-component state]])
