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
          (swap! store assoc-in [:assets stamped-path] bytes)
          stamped-path))

      (respond [_ path]
        (when-let [bytes (get-in @store [:assets path])]
          {:body bytes}))

      (list-assets [_]
        (keys (:assets @store))))))
