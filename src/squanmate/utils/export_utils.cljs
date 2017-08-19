(ns squanmate.utils.export-utils
  (:require [cljsjs.download :as download]))

(defn download-html-node-as-png [& {:keys [id-string filename]}]
  (let [node (js/document.getElementById id-string)]
    (js/html2canvas
     node
     (clj->js {:onrendered (fn [canvas]
                             (js/download
                              (.toDataURL canvas "image/png")
                              filename
                              "image/png"))}))))
