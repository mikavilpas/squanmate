(ns squanmate.services.global-colors-store
  "Operates on newmonochrome/default-settings - like maps"
  (:require [squanmate.services.storage :as storage]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]
            [reagent.core :as reagent]))

(def ^:private setting-name "global-colors")

(defn get-or-default! []
  (or (storage/get-value setting-name)
      newmonochrome/default-settings))

(defonce settings-atom
  (reagent/atom (get-or-default!)))

(defn save! [color-state]
  (storage/save setting-name color-state)
  (reset! settings-atom color-state))
