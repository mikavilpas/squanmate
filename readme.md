<img align="right"
     width="30%"
     src="https://rawgit.com/sp3ctum/squanmate/master/resources/public/readme/cubeshape-trainer.png">

![logo][logolink]

*Squanmate* is a Square-1 training tool. It can help you:

* train your cubeshape (or cubeshape parity) skills.
  * select the combinations of shapes you would like to train
  * a random scramble is generated for you. Solve away!
* learn and remember all different shapes and their names
* display cubeshape algorithms and the shapes the algorithm goes through
  * it's a good aid for memorizing algorithms!
  * link to your created visualizations from e.g. your Google Sheets
* if you use Cale Schoon's cubeshape parity method, Squanmate offers a couple of
  helpful features:
  - displays whether your entered algorithm is **even** or **odd** for that
    specific starting count position
  - allows you to find a more comfortable count position by rotating either
    layer for counting, then undoing that rotation for the algorithm. It's
    really easy to find out interesting things this way. For example, did you
    know for the kite kite case every sliceable position for either layer gives
    the same count?

**You can run it with a modern web browser right now.
Try it [here][applink]!** (the link will change with every release, 
so always open it from this readme file!)

# Development instructions
As for dependencies, you need a clojure development environment. Check out
figwheel, then start hacking something!

- To start figwheel in your REPL, evaluate this

```clojure
    (do (require 'figwheel-sidecar.repl-api)
        (figwheel-sidecar.repl-api/start-figwheel!)
        (figwheel-sidecar.repl-api/cljs-repl))
```

## Development environments
You have the option of choosing between two development environments:
- *devcards* shows tests and isolated reagent components, allowing you to
  manipulate both without seeing the main application at all (think "sandbox").
  It can be accessed at http://localhost:3449/cards.html
- *dev* is the main application, but can still be hot reloaded at will. Access
  it at http://localhost:3449/
- *prod* is not really a development environment, but I'll mention it here
  anyway. If you build prod with `(build-once prod)`, it will actually be shown
  in the place of the *dev* build. Do a `ctrl+f5` reload to force a change
  between the *prod* and *dev* "environments" just to be sure.

## How to preview live changes on multiple devices
It's possible, thanks to figwheel magic, to hot reload your code changes e.g. on
your main development desktop/laptop and your mobile device at the same time.

To do this,
- be on the same network as your host (e.g. on your home network)
- eval `(switch-to-build ...)` with the id of your build (devcards / dev)

# Build production js
In the repl, run `(build-once prod)`. The output file is tracked by git, so it
will show up as changed in your `git status`. Commit the changed file to the
repository so that the app works online with the newest version.

[logolink]: https://rawgit.com/sp3ctum/squanmate/master/resources/public/readme/logo.png
[applink]: https://cdn.rawgit.com/sp3ctum/squanmate/4.0/resources/public/index.html#/
