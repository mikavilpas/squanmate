(ns squanmate.scramblers.alg-trainer.settings-handler
  (:require [squanmate.services.global-colors-store :as global-colors-store]
            [squanmate.services.storage :as storage]))

(defn try-load-settings
  "If the user has previously saved settings, loads them and returns them (as a map)."
  []
  (storage/get-value "alg-trainer-settings"))

(defn save-settings! [state-map]
  (let [settings (->> (select-keys state-map [:selected-cases
                                              :middle-layer-settings])
                      (into {}))]
    (storage/save "alg-trainer-settings" settings)))
