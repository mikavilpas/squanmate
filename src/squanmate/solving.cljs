(ns squanmate.solving)

(enable-console-print!)

(defn- new-solver []
  ;; api:
  ;; new Worker("js/solver-worker.js").proxy()("solve")("start_state_encoded", function(err,result){[]});
  (js* "new Worker('js/solver-worker.js').proxy()('solve')"))

(defn- solve [starting-state-string]
  (let [result-atom (atom nil)
        solver (new-solver)]
    (solver starting-state-string
            (fn callback [err, result]
              (println "received (err,result): " err, result)
              (when err
                (reset! result-atom (str "failed: " err)))
              (when result
                (reset! result-atom result))))
    result-atom))

(defn example-call []
  (let [result (solve "A2B3C1D45E6F7G8H")]
    (js/console.log result)
    result))
