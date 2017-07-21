<img align="right"
     width="30%"
     src="https://rawgit.com/sp3ctum/squanmate/master/resources/public/readme/shape-visualizer.png">

![logo][logolink]

*Squanmate* is a Square-1 training tool. It can help you:

* learn and remember all different shapes and their names
* display cubeshape algorithms and the shapes the algorithm goes through
  * it's a good aid for memorizing algorithms!

You can run it with a modern web browser right now.
Try it [here][applink]!

# Development instructions
As for dependencies, you need a clojure development environment. Check out
figwheel, then start hacking something!

- To start figwheel in your REPL, evaluate this

```clojure
    (do (require 'figwheel-sidecar.repl-api)
        (figwheel-sidecar.repl-api/start-figwheel!)
        (figwheel-sidecar.repl-api/cljs-repl))
```

- open http://localhost:3449/cards.html - this uses `squanmate.test-loader` to
  load all tests that file knows about right into the devcards ui.
- changes to code should be hot reloaded in your UI, along with warnings displayed nicely

# Build production js
In the repl, run `(build-once prod)`. The output file is tracked by git, so it
will show up as changed in your `git status`. Commit the changed file to the
repository so that the app works online with the newest version.

[logolink]: https://rawgit.com/sp3ctum/squanmate/master/resources/public/readme/logo.png
[applink]: https://rawgit.com/sp3ctum/squanmate/master/resources/public/
