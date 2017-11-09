(ns squanmate.services.keyboard
  (:require [cljsjs.mousetrap]))

;; documentation: https://craig.is/killing/mice

(defn bind! [key-or-combo callback]
  (js/Mousetrap.bind key-or-combo callback))

(defn reset-bindings! []
  (js/Mousetrap.reset))
