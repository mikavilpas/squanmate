(ns squanmate.services.storage-test
  (:require [cljs.test :as t :refer [is]]
            [squanmate.services.storage :as sut])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

(deftest save-and-get-test []
  (is (= {:hello "world"}
         (do
           (sut/clear!)
           (sut/save "test-key" {:hello "world"})
           (sut/get-value "test-key")))))

(deftest contains-key-test []
  (is (true?
       (do
         (sut/clear!)
         (sut/save "test-key-2" {:hello "again"})
         (sut/contains-key? "test-key-2"))))

  (is (false?
       (do
         (sut/clear!)
         (sut/contains-key? "non-existing-key")))))
