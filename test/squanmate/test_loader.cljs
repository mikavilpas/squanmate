(ns squanmate.test-loader
  (:require [devtools.core :as devtools]

            squanmate.ui.alg-visualizer-test
            squanmate.alg.execution-test
            squanmate.alg.parser-test
            squanmate.puzzle-test
            squanmate.rotation-test
            squanmate.slicing-test
            squanmate.shapes-test
            squanmate.ui.drawing.newmonochrome-test
            squanmate.ui.shape-chooser-test
            squanmate.alg.parity-counter-test
            squanmate.solving-test
            squanmate.alg.manipulation-test
            squanmate.scramblers.shape-scrambler-test
            squanmate.shape-combinations-test
            squanmate.ui.initial-rotation-adjuster-test
            squanmate.ui.alg-importer-test
            squanmate.alg.serialization-test
            squanmate.ui.color-chooser-test
            squanmate.services.count-positions-test
            squanmate.ui.count-position-finder-test
            squanmate.ui.parity-sequences-test))

;; use this when at a breakpoint in the browser. It prints human readable and
;; pretty presentations of clojurescript things.
(js* "var p = cljs.pprint.pprint")
(js* "var l = console.log")

(devtools/set-pref! :print-config-overrides true)
(devtools/install! [:formatters :hints :async])
