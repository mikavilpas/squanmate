(ns squanmate.utils.hiccup-utils)

(defn show-when [flag component]
  [:span (when-not flag
           {:style {:visibility :hidden}})
   component])

(defn hide-when [flag component]
  [show-when (not flag) component])
