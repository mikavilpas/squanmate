(ns squanmate.services.storage
  (:require [hodgepodge.core :as hp]
            [cljs.pprint :as pprint]))

(def ^:private storage hp/local-storage)

(defn- serialize [thing]
  (with-out-str (pprint/pprint thing)))

(defn save-raw [key value]
  (hp/set-item storage (str key) value))

(defn save [key value]
  (hp/set-item storage (str key) (serialize value)))

(defn get-value [key]
  (some-> (hp/get-item storage (str key))
          (hp/deserialize)))

(defn contains-key? [key]
  (hp/contains-key? storage (str key)))

(defn delete [key]
  (hp/remove-item storage (str key)))

(defn clear!
  "use this only in tests."
  []
  (hp/clear! storage))
