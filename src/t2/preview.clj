(ns t2.preview
  (:require
   [assetnow.api]
   [assetnow.httpkit]
   [assetnow.sqlite]
   [t2.manifest]))

(assetnow.httpkit/start!)

(defonce store (assetnow.sqlite/create ":memory:"))
(assetnow.httpkit/set-store! store)
(run! #(assetnow.api/load-file store %)
      (t2.manifest/manifest))

(comment
  (assetnow.httpkit/browse)

  )
