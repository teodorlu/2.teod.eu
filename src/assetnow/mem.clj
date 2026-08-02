(ns assetnow.mem
  "in-memory asset store"
  (:require [assetnow.checksum :as checksum]
            [assetnow.protocols :as p]))

(defn create []
  (let [store (atom {})]
    (reify p/Store
      (push-asset [_ path bytes]
        (let [checksum (checksum/bytes->md5-hexdigest bytes)
              stamped-path (checksum/stamp-path path checksum)]
          (swap! store assoc-in [:serve stamped-path] bytes)
          stamped-path))

      (list-assets [_]
        (keys (:serve @store)))

      (push-fragments [_ path fragments])

      (respond [_ path]
        (when-let [bytes (get-in @store [:serve path])]
          {:body bytes})))))
