(ns squanmate.scramblers.shape-scrambler.settings-handler
  (:require [squanmate.services.storage :as storage]
            [squanmate.services.global-colors-store :as global-colors-store]))

(defn try-load-settings
  "If the user has previously saved settings, loads them and returns them (as a map)."
  []
  (let [state (storage/get-value "trainer-settings")
        color-settings (global-colors-store/get-or-default!)]
    (when (or state color-settings)
      (merge state {:draw-settings color-settings}))))

(defn save-settings! [state-map]
  (let [settings (->> (select-keys state-map [:selected-shapes
                                              :middle-layer-settings])
                      (into {}))
        color-settings (:draw-settings state-map)]
    ;; colors are saved only on their own page
    (storage/save "trainer-settings" settings)))
