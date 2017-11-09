(ns squanmate.pages.page-content)

(defmulti page (fn [app-state]
                 (-> @app-state
                     :page
                     :name)))
