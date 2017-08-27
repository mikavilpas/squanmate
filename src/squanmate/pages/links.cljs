(ns squanmate.pages.links
  (:require [squanmate.utils.route-utils :as route-utils]
            [clojure.string :as str]))

(defn- alg-or-zero [alg]
  (if (= "" alg)
    "0"
    alg))

(def ^:private encode js/encodeURIComponent)

(defn set-link-to-visualization [{:keys [top-name
                                         bottom-name
                                         initial-rotation
                                         algorithm]}]
  (route-utils/set-route!
   (str/join "/" ["shape-visualizer"
                  top-name
                  bottom-name
                  (encode (alg-or-zero initial-rotation))
                  (encode (alg-or-zero algorithm))])))
