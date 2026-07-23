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
          ;;(swap! store assoc-in [:resolve-asset path] stamped-path)
          stamped-path))

      (list-assets [_]
        (keys (:serve @store)))

      (push-fragments [_ path fragments])

      (respond [_ path]
        (when-let [bytes (get-in @store [:serve path])]
          {:body bytes})))))

(comment
  ;; resolve-all
  ;; :resolve-asset

  {:resolve-asset {"icecream.txt" "icecream.abc.txt"}
   :asset-backlinks {"icecream.txt" ["icestory.txt" 1]}
   :fragments {"icestory.txt" [(String/.getBytes "Everyone wants icecream." "UTF-8")
                               "icecream.abc.txt"]}
   :serve {"a.abc.txt" (String/.getBytes "Call me Icecream" "UTF-8")
           "icestory.txt" (byte-array 0)}}
  )
