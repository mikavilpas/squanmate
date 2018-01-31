(ns squanmate.pages.trainer
  (:require [squanmate.pages.page-content :as page-content]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.scramblers.shape-scrambler.actions :as a]
            [squanmate.scramblers.shape-scrambler.settings-handler :as settings]
            [squanmate.services.global-colors-store :as global-colors-store]
            [squanmate.services.keyboard :as keyboard]))

(def ^:private keybindings
  [{:key-combination "space"
    :description "New scramble"
    :action (fn [state]
              (-> state
                  a/set-new-random-scramble
                  keyboard/prevent-bubbling))}

   {:key-combination "shift+space",
    :description "Deselect case and set new scramble"
    :action (fn [state]
              (-> state
                  a/deselect-case-and-generate-new-scramble!
                  keyboard/prevent-bubbling))}

   {:key-combination "r r"
    :description "Repeat case with random parity"
    :action (fn [state]
              (a/set-new-repeat-scramble state))}

   {:key-combination "r s"
    :description "Repeat case with same parity"
    :action (fn [state]
              (a/set-new-scramble-with-parity state :same-relative-parity))}

   {:key-combination "r o"
    :description "Repeat case with opposite parity"
    :action (fn [state]
              (a/set-new-scramble-with-parity state :opposite-relative-parity))}])

(defn initial-state []
  (let [settings-atom (shape-scrambler/new-state :keybindings keybindings)
        maybe-saved-settings (settings/try-load-settings)]

    ;; In case saving settings makes a mistake, with this it may be possible to
    ;; recover from loading partial settings
    (when maybe-saved-settings
      (swap! settings-atom merge maybe-saved-settings))

    (add-watch settings-atom nil
               (fn [_key _ref _old-value new-state]
                 (settings/save-settings! new-state)))

    settings-atom))

(defonce page-state (initial-state))

(defn content [state]
  [:div
   [shape-scrambler/scramble-component page-state @global-colors-store/settings-atom]])

(defn- set-keyboard-bindings! [state]
  (doseq [{:keys [key-combination, action]} keybindings
          :let [function #(action state)]]
    (keyboard/bind! key-combination function)))

(defmethod page-content/page :trainer []
  (set-keyboard-bindings! page-state)
  [:div
   [content page-state]])
