(ns squanmate.test-loader
  "This namespace loads all test namespaces (they cannot be discovered unless
  explicitly loaded). The same loading can be shared between the devcards and
  command line test environments."
  (:require [devtools.core :as devtools]

            ;; Only these namespaces are loaded. If you add a new test file, it
            ;; must be added here.
            squanmate.ui.alg-visualizer-test
            squanmate.alg.execution-test
            squanmate.alg.parser-test
            squanmate.alg.puzzle-test
            squanmate.alg.rotation-test
            squanmate.alg.slicing-test
            squanmate.services.shapes-test
            squanmate.ui.drawing.newmonochrome-test
            squanmate.ui.shape-chooser-test
            squanmate.alg.parity-counter-test
            squanmate.services.solving-test
            squanmate.alg.manipulation-test
            squanmate.scramblers.shape-scrambler-test
            squanmate.services.shape-combinations-test
            squanmate.ui.rotation-adjuster-controls-test
            squanmate.ui.alg-importer-test
            squanmate.alg.serialization-test
            squanmate.ui.color-chooser-test
            squanmate.services.count-positions-test
            squanmate.ui.count-position-finder-test
            squanmate.ui.parity-sequences-test
            squanmate.ui.scramble-test
            squanmate.ui.parity-analysis-test
            squanmate.services.google-analytics-test
            squanmate.scramblers.alg-trainer-test
            squanmate.scramblers.algsets.edge-permutation-test
            squanmate.services.cube-aligner-test
            squanmate.scramblers.algsets.permute-last-layer-test
            squanmate.ui.middle-layer-controls-test
            squanmate.scramblers.shape-scrambler.predetermined-parity-scrambler-test
            squanmate.services.storage-test
            squanmate.services.color-converter-test
            squanmate.services.global-colors-store-test
            squanmate.ui.layer-selector-test
            squanmate.services.alg-insights-test
            squanmate.services.alg-insights.cubeshape-test
            squanmate.utils.either-utils-test
            squanmate.ui.alg-display-test
            squanmate.services.alg-insights.alignment-test
            squanmate.services.cubeshape-piece-swapper-test
            squanmate.scramblers.algsets.lin-corner-permutation-test
            squanmate.scramblers.algsets.cubeshape-test
            squanmate.ui.inspection-timer-test
            squanmate.scramblers.shape-scrambler.flip-layers-scrambler-test))

(devtools/install! [:formatters :hints :async])
