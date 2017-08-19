(ns squanmate.utils.route-utils
  (:require [clojure.string :as str]))

;; todo could get this from secretary config atom
(def ^:private route-prefix "#/")

(defn set-route! [route-str]
  (let [current-route (.-href js/window.location)
        [base route] (str/split current-route route-prefix)
        new-route (str/join [base route-prefix route-str])]
    (set! js/window.location.href new-route)))
