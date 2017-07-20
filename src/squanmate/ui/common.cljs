(ns squanmate.ui.common
  (:require [cljsjs.react-bootstrap]
            [reagent.core :as reagent]))

(def panel (reagent/adapt-react-class js/ReactBootstrap.Panel))
(def accordion (reagent/adapt-react-class js/ReactBootstrap.Accordion))
(def glyphicon (reagent/adapt-react-class js/ReactBootstrap.Glyphicon))
(def tooltip (reagent/adapt-react-class js/ReactBootstrap.Tooltip))
(def overlay-trigger (reagent/adapt-react-class js/ReactBootstrap.OverlayTrigger))
(def button (reagent/adapt-react-class js/ReactBootstrap.Button))

(defn- input-box [string-state placeholder]
  [:input.form-control {:on-change #(reset! string-state (-> % .-target .-value))
                        :placeholder placeholder
                        :value @string-state}])

(def tabs (reagent/adapt-react-class js/ReactBootstrap.Tabs))
(def tab (reagent/adapt-react-class js/ReactBootstrap.Tab))

(def navbar (reagent/adapt-react-class js/ReactBootstrap.Navbar))
(def nav (reagent/adapt-react-class js/ReactBootstrap.Nav))
(def navbar-header (reagent/adapt-react-class js/ReactBootstrap.Navbar.Header))
(def navbar-brand (reagent/adapt-react-class js/ReactBootstrap.Navbar.Brand))
(def nav-item (reagent/adapt-react-class js/ReactBootstrap.NavItem))
