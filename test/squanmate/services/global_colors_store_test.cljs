(ns squanmate.services.global-colors-store-test
  (:require [squanmate.services.global-colors-store :as sut]
            [cljs.test :as t :refer [is]])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(deftest save-and-get-test []
  ;; If we were to do something drastic to global colors, it would disrupt
  ;; developing in dev mode (no valid colors available)
  (let [initial (sut/get-or-default!)
        settings (assoc-in initial [:draw-mode :test-key] (rand-int 10000))]

    (sut/save! settings)

    (let [result (sut/get-or-default!)]
      (is (= (-> result :draw-mode :test-key)
             (-> settings :draw-mode :test-key))
          "Make some customization to settings, save and make sure they were saved."))))
