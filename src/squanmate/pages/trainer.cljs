(ns squanmate.pages.trainer
  (:require [squanmate.scramblers.shape-scrambler :as shape-scrambler]
            [squanmate.pages.page-content :as page-content]
            [squanmate.services.storage :as storage]
            [squanmate.scramblers.shape-scrambler.actions :as a]))

(defn initial-state []
  (let [settings-atom (shape-scrambler/new-state)
        maybe-saved-settings (a/try-load-settings)]
    (when maybe-saved-settings
      (swap! settings-atom merge maybe-saved-settings))
    (add-watch settings-atom nil
               (fn [_key _ref _old-value new-state]
                 (a/save-settings! new-state)))
    settings-atom))

(defonce page-state (initial-state))

(defn content []
  [:div
   [shape-scrambler/scramble-component page-state]])

(defmethod page-content/page :trainer []
  [:div
   [content]])
