(ns squanmate.services.storage
  (:require [hodgepodge.core :as hp]))

(defn save [key value]
  (hp/set-item hp/local-storage (str key) value))

(defn get-value [key]
  (some-> (hp/get-item hp/local-storage (str key))
          (hp/deserialize)))

(defn contains-key? [key]
  (hp/contains-key? hp/local-storage (str key)))

(defn delete [key]
  (hp/remove-item hp/local-storage (str key)))

(defn clear!
  "use this only in tests."
  []
  (hp/clear! hp/local-storage))
