(ns squanmate.services.web-worker)

(defrecord WebWorker [worker-object
                      worker-proxy])

;; constructor
(defn new-worker [script-path]
  ;; api:
  ;; var worker = new Worker("js/solver-worker.js")
  ;; worker.proxy(worker)("solve")("start_state_encoded", function(err,result){[]});
  (let [worker (new js/Worker script-path)
        worker-proxy-fn (-> worker (aget "proxy"))
        worker-proxy (worker-proxy-fn worker)]
    (->WebWorker worker worker-proxy)))

(defn terminate [w]
  ;; Allow running and terminating the worker after it's done, if desired.
  ;; Otherwise the worker will continue to live on, and eventually crash the
  ;; browser/tab when they pile up.
  (.terminate (:worker-object w)))

(defn- get-function [w name]
  ((:worker-proxy w) name))

(defn- run-async [w function-name args callback]
  (let [f (get-function w function-name)
        new-args (conj args callback)]
    (apply f new-args)))

(defn run-and-terminate [w function-name args callback]
  (let [die #(terminate w)
        new-callback (comp die callback)]
    (run-async w function-name args new-callback)))
