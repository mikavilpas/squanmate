(ns squanmate.utils.either-utils-test
  (:require [cats.monad.either :as either]
            [clojure.test :as t :refer [is]]
            [squanmate.utils.either-utils :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(deftest list-of-rights->right-list-test []
  (is (= (either/right [1 2 3])
         (sut/list-of-eithers->either-list [(either/right 1)
                                            (either/right 2)
                                            (either/right 3)]))
      "combines rights")

  (is (= (either/left 4)
         (sut/list-of-eithers->either-list [(either/right 1)
                                            (either/left 2)
                                            (either/right 3)
                                            (either/left 4)]))
      "otherwise returns the last left value")

  (is (= (either/right [])
         (sut/list-of-eithers->either-list []))
      "empty case"))
