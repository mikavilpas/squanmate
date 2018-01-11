(ns squanmate.services.color-converter
  ;; NB: Cannot require tinycolor! it will not compile under advanced
  ;; optimizations. The "tinycolor" global is available though, it comes from
  ;; cljsjs/react-color
  (:require [cljsjs.react-color]))

;; a color can be either a named color or an RGB value like [r g b]

(def ^:private named-colors
  ;; internally this namespace stores colors as rgb vectors because these are
  ;; what are used for rendering by quil.
  {:gray [169 169 169]
   :white [253 253 253]
   :yellow [255, 254, 69]

   :orange [255, 166, 69]
   :blue [99, 96, 255]
   :red [255, 69, 69]
   :green [69, 245, 69]})

(defn- rgb->tinycolor [[r g b]]
  (new js/tinycolor (str "rgb " r " " g " " b)))

(defn color-name->color [color]
  (get named-colors color
       ;; If not found, the color must be a literal rgb value. Return it.
       color))

(defn color->hex [color]
  (let [tc (-> color
               color-name->color
               rgb->tinycolor)]
    (.toHexString tc)))
