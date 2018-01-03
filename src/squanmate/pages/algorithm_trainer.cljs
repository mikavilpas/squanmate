(ns squanmate.pages.algorithm-trainer
  (:require [squanmate.pages.page-content :as page-content]
            [squanmate.scramblers.alg-trainer :as alg-trainer]
            [squanmate.scramblers.alg-trainer.settings-handler :as settings]
            [squanmate.services.global-colors-store :as global-colors-store]))

(defn initial-state []
  (let [settings-atom (alg-trainer/new-default-state)
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
  [:div
   [content]])
