(ns squanmate.scramblers.alg-trainer.settings-handler
  (:require [squanmate.services.global-colors-store :as global-colors-store]
            [squanmate.services.storage :as storage]))

(defn try-load-settings
  "If the user has previously saved settings, loads them and returns them (as a map)."
  []
  (let [state (storage/get-value "alg-trainer-settings")
        color-settings (global-colors-store/get-or-default!)]
    (when (or state color-settings)
      (merge state {:draw-settings color-settings}))))

(defn save-settings! [state-map]
  (let [settings (->> (select-keys state-map [:selected-cases
                                              :middle-layer-settings])
                      (into {}))
        color-settings (:draw-settings state-map)]
    ;; colors are saved only on their own page
    (storage/save "alg-trainer-settings" settings)))
