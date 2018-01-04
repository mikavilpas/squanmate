(ns squanmate.ui.layer-selector-test
  (:require [clojure.test :as t :refer [is]]
            [squanmate.ui.layer-selector :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(defn- new-state []
  (atom {:selected-shapes (hash-set)}))

(deftest select-filtered-shapes-test []
  (let [a (new-state)]
    (sut/select-all-filtered-shapes! a "star")

    (is (= {:selected-shapes
            (hash-set #{"star" "six-two"}
                      #{"star" "four-four"}
                      #{"star" "five-three"}
                      #{"star" "eight"}
                      #{"star" "seven-one"})}
           @a)

        "Selects all star shapes"))

  (let [a (new-state)]
    (sut/select-all-filtered-shapes! a "star")
    (sut/select-no-filtered-shapes! a "star")
    (is (= (deref (new-state)) @a)
        "Select all star shapes -> deselect all star shapes"))

  (let [a (new-state)]
    (sut/select-or-deselect! a #{"star" "six-two"})
    (sut/select-all-filtered-shapes! a "star")
    (is (= {:selected-shapes #{#{"star" "six-two"}
                               #{"star" "seven-one"}
                               #{"star" "eight"}
                               #{"star" "five-three"}
                               #{"star" "four-four"}}}
           @a)
        "Select a single star case, then select all the rest"))

  (let [a (new-state)]
    (sut/select-or-deselect! a #{"star" "six-two"})
    (sut/select-no-filtered-shapes! a "star")
    (is (= (deref (new-state)) @a)
        "Select a single star case, then select none of the star cases")))
