(ns squanmate.pages.trainer
  (:require [squanmate.pages.page-content :as page-content]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.scramblers.shape-scrambler.actions :as a]
            [squanmate.scramblers.shape-scrambler.settings-handler :as settings]
            [squanmate.services.global-colors-store :as global-colors-store]
            [squanmate.services.keyboard :as keyboard]))

(defn initial-state []
  (let [settings-atom (shape-scrambler/new-state)
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
  (keyboard/bind! "space" (keyboard/prevent-bubbling
                           #(a/set-new-random-scramble state)))
  (keyboard/bind! "shift+space" (keyboard/prevent-bubbling
                                 #(a/deselect-case-and-generate-new-scramble! state)))

  (keyboard/bind! "r r" #(a/set-new-repeat-scramble state))
  (keyboard/bind! "r s" #(a/set-new-scramble-with-parity state :same-relative-parity))
  (keyboard/bind! "r o" #(a/set-new-scramble-with-parity state :opposite-relative-parity)))

(defmethod page-content/page :trainer []
  (set-keyboard-bindings! page-state)
  [:div
   [content page-state]])
