(ns squanmate.pages.trainer
  (:require [squanmate.pages.page-content :as page-content]
            [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.scramblers.shape-scrambler.actions :as a]
            [squanmate.scramblers.shape-scrambler.settings-handler :as settings]))

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

(defn content []
  [:div
   [shape-scrambler/scramble-component page-state]])

(defmethod page-content/page :trainer []
  [:div
   [content]])
