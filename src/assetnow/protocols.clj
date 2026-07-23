(ns assetnow.protocols
  (:require [assetnow.checksum :as checksum]))

(defprotocol Store
  (push-asset [this url asset-bytes])
  (list-assets [this])
  (push-fragments [this url fragments])
  (respond [this url]))
