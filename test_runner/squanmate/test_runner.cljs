(ns squanmate.test-runner
  "This is the main namespace for running command line tests."
  (:require
   ;; load all test namespaces
   [squanmate.test-loader :as test-loader]

   [doo.runner :as runner]
   [cljs.test :as test]))

(enable-console-print!)

;; needs to be executed when this file is evaluated. This is required by
(runner/set-entry-point!
 (fn []
   (test/run-all-tests #"^(?!(squanmate.solving-test)$).*$")))
