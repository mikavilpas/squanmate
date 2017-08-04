(ns squanmate.solving
  (:require [cljs.core.async :refer [chan close! timeout put!]]
            [servant.core :as servant]
            [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]))


(def worker-count 1)

;; The web worker api works by requiring a separate script file, then executing
;; that in a new thread, and finally sending its output back to the main script
;; (browsing session script).
;;
;; The servant library simplifies this a bit, but it still requires a separate
;; script file to work.
;; For now let's keep it simple and just use this same script twice. This means
;; the large-ish main script file that contains the entire squanmate app. But
;; requiring it again should mean that it also has been loaded once, and it
;; should be retrieved from the browser's cache, making "fetching" it instant.
(def worker-script "js/compiled/squanmate.js")

;; We need to make sure that only the main script will spawn the servants.
(when-not (servant/webworker?)
  ;; We keep all the servants in a buffered channel.
  (def servant-channel (servant/spawn-servants worker-count worker-script)))


(defservantfn some-random-fn [a b]
  (+ a b))
;; This can also call other functions within the scope!

(defn make-it-funny [not-funny]
  (str "Hahahah:" not-funny))

(defservantfn servant-with-humor [your-joke]
  (make-it-funny your-joke))

(def result-channel (servant/servant-thread servant-channel
                                            servant/standard-message
                                            some-random-fn
                                            ;; args follow
                                            5
                                            6))
