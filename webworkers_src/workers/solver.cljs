(ns workers.solver
  (:require [cljs.core.async :refer [chan close! timeout put!]]
            [servant.core :as servant]
            [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]))

(defservantfn some-random-fn [a b]
  (+ a b))
;; This can also call other functions within the scope!

(defn make-it-funny [not-funny]
  (str "Hahahah:" not-funny))

(defservantfn servant-with-humor [your-joke]
  (make-it-funny your-joke))

(js/console.log "hello from solver 2!")
