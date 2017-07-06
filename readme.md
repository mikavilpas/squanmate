# Development dependencies
- docker
- docker-compose
- cubeshape by Zsófia Balogh https://github.com/sp3ctum/cubeshape - I created my
  own fork to make sure the code doesn't disappear anywhere. I have no relation
  with that project, and want to take no credit for it.

# Development setup instructions
- run `docker-compose up` to start the cubeshape Square-1 drawing web app.
- you can test it by loading http://localhost:9292/cubeshape/cececece?colors=yrb_yb_ybo_yo_yog_yr_ygr_yg in your web browser.
- evaluate this

    (do (require 'figwheel-sidecar.repl-api)
        (figwheel-sidecar.repl-api/start-figwheel!)
        (figwheel-sidecar.repl-api/cljs-repl))

- open http://localhost:3449/cards.html
- changes to code should be hot reloaded in your UI, along with warnings displayed nicely

