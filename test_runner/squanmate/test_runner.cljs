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
   ;; ignore squanmate.solving-test for now. It has dependencies to
   ;; web-worker-proxies that are not included in the js build (they are loaded
   ;; externally in the browser). This causes them to rightfully not exist when
   ;; testing.
   ;;
   ;; also: this test api is based on macros, which means the argument must be a
   ;; literal string.
   (test/run-all-tests #"^squanmate.(?!(solving-test)$).*$")))
