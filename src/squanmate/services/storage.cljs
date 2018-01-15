(ns squanmate.services.storage
  (:require [hodgepodge.core :as hp]
            [clojure.walk :as walk]))

(def ^:private storage hp/local-storage)

(defn- record-to-map [thing]
  (if (record? thing)
    (into {} thing)
    thing))

(defn- serialize [thing]
  ;; Records in clojure are not serializable. They will be serialized as
  ;; #squanmate.foo.SomeType{:a 3} structure, which cannot be read back by the
  ;; reader.
  ;;
  ;; Fortunately each record is also a map, so they can be coerced into
  ;; one (losing the type information of the record). This way this limitation
  ;; can be avoided.
  (->> thing
       (walk/postwalk record-to-map)
       pr-str))

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
