(ns squanmate.pages.scramble-inspector
  (:require [squanmate.ui.scramble :as scramble]))

(defonce state (scramble/default-state))

(defn content []
  [scramble/component state])
