(ns squanmate.services.keyboard
  (:require [cljsjs.mousetrap]))

;; documentation: https://craig.is/killing/mice

(defn prevent-bubbling [&]
  ;; Returning false will cause mousetrap to stop the browser's default action
  ;; defined for the current key binding.
  ;;
  ;; This can be used to bind the space or enter keys to a button. Normally the
  ;; keyboard event would bubble up, which would cause the keybinding to be
  ;; triggered in addition to the button being clicked (via space or enter)
  false)

(defn bind! [key-or-combo callback]
  (js/Mousetrap.bind key-or-combo callback))

(defn reset-bindings! []
  (js/Mousetrap.reset))
