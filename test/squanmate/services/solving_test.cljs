(ns squanmate.services.solving-test
  (:require [squanmate.services.solving :as solving]
            [clojure.test :as t :refer [is async]]
            [cljs.test :as t :include-macros true]
            [cljs.core.async :refer [<! timeout]]
            [squanmate.alg.puzzle :as puzzle]
            [squanmate.alg.execution :as execution]
            [squanmate.services.shapes :as shapes]
            [cats.monad.either :as either]
            [cats.core :as m]
            [squanmate.alg.puzzle :as p]
            [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [deftest defcard-rg]]
   [cljs.core.async.macros :refer [go]]))

(defcard-rg overview-introduction-explanation
  [:div
   [:div "Solving the puzzle is done using Jaap Scherphuis's solver, based on
  code available from "
    [:a {:href "https://www.jaapsch.net/puzzles/square1.htm"}
     "his site"] ". "
    "The original code is written in the C++ language. To use this solver in a
    browser (JavaScript environment), I compiled it to JavaScript using a
    cross(?) compiler called Cheerp. I made an interface to the solver for using
    only the first found solution (otherwise it searches for many, trying to
    find the best)."]

   [:div "The solver is lauched in a Web Worker, which on modern machines should
   mean a separate thread. The solver itself only takes less than half a second
   to calculate a solution in my tests, but this ensures two things:"
    [:ul
     [:li "The UI is responsive (the solver doesn't block the UI thread)"]
     [:li "If the solver takes a long time to find a solution (hasn't occurred
     yet, but just in case), the UI can show a loading icon, or interrupt the
     solver, or something else."]]]

   [:div "An interesting quirk of Jaap's solver is that it can only solve puzzle
   permutations which are " [:strong "sliceable"] "."]
   "To remove this limitation, we do this: "
   [:ul
    [:li "make a random initial rotation of both layers that makes the puzzle sliceable"]
    [:li "give the new, sliceable puzzle to Jaap's solver"]
    [:li "combine the initial rotation with the algorithm received from Jaap's solver"]]])

(deftest convert-to-state-string-test []
  (is (= "A1B2C3D45E6F7G8H-"
         (solving/convert-to-state-string puzzle/square-square false))
      "should be able to convert square square to solver notation"))

(deftest worker-poc-test []
  (async done
         (go
           (let [result-atom (solving/solve-state-string "A2B3C1D45E6F7G8H"
                                                         :initial-rotation nil
                                                         :result-atom (reagent/atom nil))]
             (<! (timeout 500))
             (is (= "(0,2)/(-5,4)/(5,2)/(-3,0)/(0,3)/(-5,1)/(-1,2)/(-3,-2)"
                    @result-atom)
                 "solve an example from the readme file of Jaap's solver")
             (done)))))

(defn scrambled-puzzle [alg]
  (let [result (execution/transformation-result puzzle/square-square alg)]
    (assert (either/right? result))
    (:puzzle (deref result))))

(defn should-be-solved-with [scramble-alg expected-solve-alg done]
  (go
    (let [result-atom (solving/solve (scrambled-puzzle scramble-alg) true)]
      (<! (timeout 500))
      (is (= expected-solve-alg @result-atom)
          (str "should solve " scramble-alg " with " expected-solve-alg))
      (done))))

(deftest solve-kite-kite []
  (async done
         (should-be-solved-with "/3" "(-3,0)/" done)))

(deftest solve-non-sliceable-position []
  "An 'non-sliceable position' is one where the puzzle can't be sliced (a corner
  piece blocks the middle seam). This is a special position for Jaap's solver,
  because it actually requires all inputs to not be in this position.

  To work around this, a random twist of both layers is performed that takes the
  puzzle to a position that can is sliceable. This is then prepended to the
  resulting solve algorithm, so it will look like the solver was able to handle
  this case."
  (async done
         (go
           (let [scramble-alg "/3/1,3/1"
                 start-puzzle (scrambled-puzzle scramble-alg)
                 result-atom (solving/solve start-puzzle)]
             (<! (timeout 500))
             (is (= p/square-square
                    (-> start-puzzle
                        (execution/transformation-result @result-atom)
                        m/extract
                        :puzzle))
                 (str "should solve " scramble-alg " using " @result-atom))
             (done)))))
