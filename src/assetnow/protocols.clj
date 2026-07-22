(ns assetnow.protocols
  (:require [assetnow.checksum :as checksum]))

(defprotocol Store
  (push-asset [this url asset-bytes])
  (list-assets [this])
  (respond [this url]))
