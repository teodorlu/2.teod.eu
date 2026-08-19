(ns t2.preview
  (:require
   [assetnow.api]
   [assetnow.httpkit]
   [assetnow.sqlite]
   [t2.index]))

(assetnow.httpkit/start!)

(defonce store (assetnow.sqlite/create ":memory:"))
(assetnow.httpkit/set-store! store)
(run! #(assetnow.api/load-file store %)
      (t2.index/manifest))

(comment
  (assetnow.httpkit/browse)

  )
