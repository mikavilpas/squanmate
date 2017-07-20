(ns squanmate.ui.main-ui-test
  (:require [squanmate.pages.main-ui :as main-ui])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg]]))

(defcard-rg main-ui-test
  [main-ui/main-ui])
