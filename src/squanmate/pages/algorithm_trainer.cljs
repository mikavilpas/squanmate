(ns squanmate.pages.algorithm-trainer
  (:require [squanmate.pages.page-content :as page-content]
            [squanmate.scramblers.alg-trainer :as alg-trainer]
            [squanmate.scramblers.alg-trainer.scramble-generation :as scramble-generation]
            [squanmate.scramblers.alg-trainer.settings-handler :as settings]
            [squanmate.services.global-colors-store :as global-colors-store]
            [squanmate.services.keyboard :as keyboard]))

(def ^:private keybindings
  [{:key-combination "space"
    :description "New scramble"
    :action (fn [state]
              (-> state
                  scramble-generation/set-new-scramble!
                  keyboard/prevent-bubbling))}])

(defn- set-keyboard-bindings! [state]
  (doseq [{:keys [key-combination, action]} keybindings
          :let [function #(action state)]]
    (keyboard/bind! key-combination function)))

(defn initial-state []
  (let [settings-atom (alg-trainer/new-default-state :keybindings keybindings)
        maybe-saved-settings (settings/try-load-settings)]

    (when maybe-saved-settings
      (swap! settings-atom merge maybe-saved-settings))

    (add-watch settings-atom nil
               (fn [_key _ref _old-value new-state]
                 (settings/save-settings! new-state)))

    settings-atom))

(defonce page-state (initial-state))

(defn content []
  [:div
   [alg-trainer/trainer-component page-state @global-colors-store/settings-atom]])

(defmethod page-content/page :algorithm-trainer []
  (set-keyboard-bindings! page-state)
  [:div
   [content]])
