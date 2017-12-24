(ns squanmate.ui.case-counter)

;; shared way to count the percentage for all possible UIs
(defn selected-cases-percentage [selected-cases
                                 total-cases]
  (-> (* 100 (/ selected-cases
                total-cases))
      (.toFixed 2)))

;; default ui component. create your own if you need a specific text
(defn selected-cases-counter [selected-cases
                              total-cases]
  (let [percentage (selected-cases-percentage selected-cases
                                              total-cases)]
    [:div (str selected-cases " / " total-cases " cases selected (" percentage " %).")]))
