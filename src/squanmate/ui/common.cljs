(ns squanmate.ui.common
  (:require [cljsjs.react-bootstrap]
            [reagent.core :as reagent]))

(def panel (reagent/adapt-react-class js/ReactBootstrap.Panel))
(def accordion (reagent/adapt-react-class js/ReactBootstrap.Accordion))
(def glyphicon (reagent/adapt-react-class js/ReactBootstrap.Glyphicon))
(def tooltip (reagent/adapt-react-class js/ReactBootstrap.Tooltip))
(def overlay-trigger (reagent/adapt-react-class js/ReactBootstrap.OverlayTrigger))

(defn- input-box [string-state placeholder]
  [:input.form-control {:on-change #(reset! string-state (-> % .-target .-value))
                        :placeholder placeholder
                        :value @string-state}])
