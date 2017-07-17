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
