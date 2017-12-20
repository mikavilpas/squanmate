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
            squanmate.services.storage-test))

(devtools/install! [:formatters :hints :async])
