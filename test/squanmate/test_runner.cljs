(ns squanmate.test-runner
  "This is the main namespace for running command line tests."
  (:require
   ;; load all test namespaces
   squanmate.test-loader

   [doo.runner :refer-macros [doo-tests]]))

;; now all test namespaces are loaded, and this can discover all tests in them
(doo.runner/doo-all-tests)
