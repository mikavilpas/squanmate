<img align="right"
     width="30%"
     src="https://rawgit.com/sp3ctum/squanmate/master/resources/public/readme/cubeshape-trainer.png">

![logo][logolink]

*Squanmate* is a Square-1 trainer, analysis and general learning tool.

**You can run it with a modern web browser right now.
Try it [here][applink]!** (the link will change with every release,
so always open it from this readme file!)


Squanmate can help you:

* **Train your cubeshape (or cubeshape parity) skills**
  * select the combinations of shapes you would like to train
  * a random scramble is generated for you. Solve away!
  * repeat the selected case with the same/opposite parity, to train both cases
    effectively!
* **Train algorithms**
  * like with cubeshape cases, select the cases you want to train
  * currently supports **edge permutation (EP)** and **permute last layer
    (PLL)** cases.
  * cases are split into even and odd parity cases for your convenience.
* **Learn and remember all different shapes and their names**
* **Inspect a scramble**
  * check whether the scramble gives an odd or even **parity count** and see
    exactly what the count consists of.
  * rotate the scramble how you like
  * link to a scramble so that it can be returned to later.
* **Display cubeshape algorithms and the shapes the algorithm goes through**
  * it's a good aid for memorizing algorithms!
  * link to your created visualizations from e.g. your Google Sheets
* **Cubeshape parity helpers**. If you use Cale Schoon's cubeshape parity
    method, Squanmate offers a couple of helpful features:
  - displays whether your entered algorithm is **even** or **odd** for that
    specific starting count position
  - allows you to find a more comfortable count position by rotating either
    layer for counting, then undoing that rotation for the algorithm. It's
    really easy to find out interesting things this way. For example, did you
    know that for all kite cases every sliceable position for a kite layer
    gives the same count?
* **Import a cubeshape algorithm**. Say you have a cool alg that you would like
  to inspect or verify with Squanmate. Enter your alg into the *Cubeshape
  algorithm importer* and it will be recognized with minimal work on your part.
  I use this to import the cubeshape algs originally made for the more popular
  blind tracing CSP method.
* **Inspect all possible parity count positions for all shapes**. When counting
  your parity, it's possible to start at multiple positions and still get the
  same result. Having options for counting will come in handy when your scramble
  orients the layers uncomfortably.
* **Train your skills in recognizing parities in color sequences**. See the
  Squanmate algorithm document below for details on how color sequences
  contribute to the parity of the puzzle.

## Related Square-1 learning resources
- I have collected all cubeshape parity algorithms for the Cale
  Schoon method. If you use that method, take a look at
  [my algorithms at Google Sheets][my-algs]
- I use a simple but effective method for solving the Square-1. If you want to
  take a look, here are [my Lin method algs][my-lin-algs]

# Development instructions

Master:
[![Build Status: master](https://travis-ci.org/sp3ctum/squanmate.svg?branch=master)](https://travis-ci.org/sp3ctum/squanmate)
Develop:
[![Build Status: develop](https://travis-ci.org/sp3ctum/squanmate.svg?branch=develop)](https://travis-ci.org/sp3ctum/squanmate)

As for dependencies, you need a clojure development environment. Check out
figwheel, then start hacking something!

- To start figwheel in your REPL, evaluate this

```clojure
    (do (require 'figwheel-sidecar.repl-api)
        (figwheel-sidecar.repl-api/start-figwheel!)
        (figwheel-sidecar.repl-api/cljs-repl))
```

## Java 9

If you’re using JDK 9, you need to include the JVM options `"--add-modules" "java.xml.bind"` 
in your _project.clj_. Add it just to the existing one that the line fully reads 
`:jvm-opts ["--add-modules" "java.xml.bind" "-Xmx1G"]`. Do not commit this, as it breaks 
<Java 9 JVMs. 

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

## How to run tests
The tests are compiled to JavaScript and then run in a headless environment. To
run the tests:

- Install PhantomJS from http://phantomjs.org/download.html
- `lein doo once`
- you can also run `lein doo` to keep running the tests whenever the files'
  contents change

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
[applink]: https://cdn.rawgit.com/sp3ctum/squanmate/6.4/resources/public/index.html#/
[my-algs]: https://docs.google.com/spreadsheets/d/1r0LN41RGKI4oAvD9rCmmc-A5UBpNPvsEOb7eWaEtIFo/edit?usp=sharing
[my-lin-algs]: https://docs.google.com/spreadsheets/d/1VxhlAqf0z91Cqqo-EEmUuZhJP4NjlA741fjOOZLrcns/edit?usp=sharing
