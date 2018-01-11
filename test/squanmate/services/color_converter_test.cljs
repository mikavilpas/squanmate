(ns squanmate.services.color-converter-test
  (:require [squanmate.services.color-converter :as sut]
            [cljs.test :as t :refer [is]])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))


(deftest convert-color-names []
  (is (= "#fdfdfd" (sut/color->hex :white)))
  (is (= "#fffe45" (sut/color->hex :yellow)))
  (is (= "#6360ff" (sut/color->hex :blue)))
  (is (= "#45f545" (sut/color->hex :green)))
  (is (= "#ffa645" (sut/color->hex :orange)))
  (is (= "#ff4545" (sut/color->hex :red)))
  (is (= "#a9a9a9" (sut/color->hex :gray))))
