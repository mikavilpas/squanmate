(ns squanmate.services.global-colors-store
  "Operates on newmonochrome/default-settings like maps"
  (:require [squanmate.services.storage :as storage]
            [squanmate.ui.drawing.newmonochrome :as newmonochrome]))

(def ^:private setting-name "global-colors")

(defn save! [color-state]
  (storage/save setting-name color-state))

(defn get-or-default! []
  (or (storage/get-value setting-name)
      newmonochrome/default-settings))
