(defproject squanmate "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [devcards "0.2.3"]

                 ;; upgrading from reagent 0.7.0 to 0.8.0-alpha1 didn't work
                 ;; unless I paste its react dependencies here too.
                 [reagent "0.8.0-alpha1"]
                 [cljsjs/react "15.6.1-2"]
                 [cljsjs/react-dom "15.6.1-2"]
                 ;; [cljsjs/react-dom-server "15.6.1-2"]
                 [cljsjs/create-react-class "15.6.0-3"]

                 [funcool/cats "2.1.0"]
                 [the/parsatron "0.0.7"]
                 [cljsjs/react-select "1.0.0-rc.3"]
                 [cljsjs/react-bootstrap "0.31.0-0"]
                 [quil "2.6.0"]
                 [cljsjs/download "1.4.6-0"]
                 [secretary "1.2.3"]
                 [org.clojure/math.combinatorics "0.1.4"]
                 [cljsjs/google-analytics "2015.04.13-0"]
                 [cljsjs/mousetrap "1.5.3-0"]
                 [hodgepodge "0.1.3"]

                 ;; provides tinycolor internally!
                 [cljsjs/react-color "2.13.1-0"]]

  :plugins [[lein-figwheel "0.5.13"]
            [lein-cljsbuild "1.1.5" :exclusions [org.clojure/clojure]]
            [lein-doo "0.1.8"]]

  :doo {:build "test"
        :alias {:default [:phantom]}}

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "devcards"
                        :source-paths ["src" "test"]
                        :figwheel {:devcards true ;; <- note this
                                   ;; :open-urls will pop open your application
                                   ;; in the default browser once Figwheel has
                                   ;; started and complied your application.
                                   ;; Comment this out once it no longer serves you.
                                   :open-urls ["http://localhost:3449/cards.html"]
                                   :websocket-host :js-client-host}
                        :compiler {:main       "squanmate.test-loader"
                                   :asset-path "js/compiled/devcards_out"
                                   :output-to  "resources/public/js/compiled/squanmate_devcards.js"
                                   :output-dir "resources/public/js/compiled/devcards_out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src" "test" "test_runner"]
                        :compiler {:output-to "resources/public/js/testable.js"
                                   :main squanmate.test-runner
                                   :output-dir "resources/public/js/compiled/test_runner_out"
                                   :optimizations :none}}

                       {:id "dev"
                        :source-paths ["src"]
                        :figwheel {:websocket-host :js-client-host}
                        :compiler {:main       "squanmate.core"
                                   :asset-path "js/compiled/out"
                                   :output-to  "resources/public/js/compiled/squanmate.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :source-map-timestamp true }}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:main       "squanmate.core"
                                   :asset-path "js/compiled/out"
                                   :output-to  "resources/public/js/compiled/squanmate.js"
                                   :optimizations :advanced
                                   :source-map "resources/public/js/compiled/squanmate.map.js"
                                   :output-wrapper true

                                   :externs ["externs/quil.externs.js"]

                                   ;; these are very useful when there is an odd
                                   ;; bug in the optimized production code, but
                                   ;; no such bug exists in the development
                                   ;; code.

                                   ;; :print-input-delimiter true
                                   ;; :pretty-print true
                                   ;; :pseudo-names true
                                   }}]}

  :figwheel { :css-dirs ["resources/public/css"] }

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.2"]
                                  [figwheel-sidecar "0.5.13"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   ;; need to add dev source path here to get user.clj loaded
                   :source-paths ["src" "dev"]
                   ;; for CIDER
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                   :repl-options {; for nREPL dev you really need to limit output
                                  :init (set! *print-length* 50)
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}

  ;; work around a compilation error from math.combinatorics
  :jvm-opts ["-Xmx1G"])
