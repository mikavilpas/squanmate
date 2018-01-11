(ns squanmate.ui.alg-display
  (:require [cats.monad.either :as either]
            [squanmate.alg.execution :as execution]
            [squanmate.alg.parser :as parser]
            [squanmate.puzzle :as p]
            [squanmate.services.alg-insights :as alg-insights]
            [squanmate.utils.either-utils :as eu]
            [cats.core :as m]
            [squanmate.alg.serialization :as serialization]))

(defn- failed-scramble [error]
  [:div "scramble error :("
   [:div (str error)]])

(defn- algorithm-step [s]
  [:span (serialization/step-to-str s)])

(defn- add-insights [tokens parts]
  tokens)

;; todo move this to insights namespace and test it!
;; todo move this to insights namespace and test it!
;; todo move this to insights namespace and test it!
;; todo move this to insights namespace and test it!

(defn- successful-scramble [display-tokens execution-steps]
  (let [parts (alg-insights/entered-and-left-cubeshape execution-steps)
        tokens (into [:div]
                     (map algorithm-step display-tokens))]
    (js/console.log display-tokens)
    (js/console.log execution-steps)
    (js/console.log parts)
    (add-insights tokens parts)))

(defn rich-scramble-display
  "Precondition: the `scramble-string` must start at square-square."
  [scramble-string]
  #_(either/branch
     (alg-insights/alg-with-cubeshape-status-highlighted scramble-string)
     (m/mlet [display-tokens (parser/parse scramble-string)
              execution-steps (scramble-steps scramble-string)]
             (m/return
              [successful-scramble display-tokens execution-steps]))
     failed-scramble
     (fn [alal]
       [successful-scramble alal])))
