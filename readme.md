![logo][logolink]

*Squanmate* is a Square-1 training tool. It can help you:

<img align="right" src="https://rawgit.com/sp3ctum/squanmate/feature/preview-image/resources/readme/shape-visualizer.png">

* learn and remember all different shapes and their names
* display cubeshape algorithms and the shapes the algorithm goes through
  * it's a good aid for memorizing algorithms!

You can run it with a modern web browser right now.
Try it [here][applink]!

# Development dependencies
- a clojure development environment

# Development setup instructions
- evaluate this

    (do (require 'figwheel-sidecar.repl-api)
        (figwheel-sidecar.repl-api/start-figwheel!)
        (figwheel-sidecar.repl-api/cljs-repl))

- open http://localhost:3449/cards.html
- changes to code should be hot reloaded in your UI, along with warnings displayed nicely

# Build production js
In the repl, run `(build-once prod)`

[logolink]: https://rawgit.com/sp3ctum/squanmate/develop/resources/readme/logo.png
[applink]: https://rawgit.com/sp3ctum/squanmate/develop/resources/public/
