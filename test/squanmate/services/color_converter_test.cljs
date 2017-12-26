(ns squanmate.services.color-converter-test
  (:require [squanmate.services.color-converter :as sut]
            [cljs.test :as t :refer [is]])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))


(deftest convert-color-names []
  (is (= "#ffffff" (sut/color->hex :white)))
  (is (= "#fffe44" (sut/color->hex :yellow)))
  (is (= "#625fff" (sut/color->hex :blue)))
  (is (= "#47f543" (sut/color->hex :green)))
  (is (= "#ffa743" (sut/color->hex :orange)))
  (is (= "#ff4343" (sut/color->hex :red)))
  (is (= "#aaaaaa" (sut/color->hex :gray))))
