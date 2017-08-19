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
(def label (reagent/adapt-react-class js/ReactBootstrap.Label))
(def popover (reagent/adapt-react-class js/ReactBootstrap.Popover))
(def well (reagent/adapt-react-class js/ReactBootstrap.Well))
(def alert (reagent/adapt-react-class js/ReactBootstrap.Alert))

;; todo these would look nice, but they are not yet included in cljsjs/react-bootstrap
;; (def toggle-button (reagent/adapt-react-class js/ReactBootstrap.ToggleButton))
;; (def toggle-button-group (reagent/adapt-react-class js/ReactBootstrap.ToggleButtonGroup))

(def checkbox (reagent/adapt-react-class js/ReactBootstrap.Checkbox))

(defn shape-preview-image-url [shape-key]
  (str "./img/shape-thumbnails/" shape-key ".png"))
