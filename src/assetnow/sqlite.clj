(ns assetnow.sqlite
  "sqlite-backed asset store"
  (:require [assetnow.checksum :as checksum]
            [assetnow.protocols :as p]
            [sqlite4clj.core :as d]))

;; fragments is a sequence of references (path) to hashed bytes
;; all assets are stored as hashed bytes
;; all static fragment *parts* are also stored as hashed bytes

;; sha256
;; uuids? auto enumerate?

(defn init-schema! [db]
  (d/q (:writer db)
       ["CREATE TABLE IF NOT EXISTS asset_bytes(
           sha256_hex TEXT PRIMARY KEY,
           bytes BLOB NOT NULL
         ) WITHOUT ROWID"])
  (d/q (:writer db)
       ["CREATE TABLE IF NOT EXISTS asset_paths(
           path TEXT PRIMARY KEY,
           stamped_path TEXT NOT NULL UNIQUE,
           sha256_hex TEXT NOT NULL REFERENCES asset_bytes(sha256_hex)
         ) WITHOUT ROWID"])
  db)

(defn create []
  (let [db (init-schema!
            (d/init-db! ":memory:"
                        {:pool-size 4
                         :pragma    {:foreign_keys true}}))]
    (reify p/Store
      (push-asset [_ path bytes]
        (let [sha256-hex (checksum/bytes->sha256-hexdigest bytes)
              stamped-path (checksum/stamp-path path sha256-hex)]
          (d/with-write-tx [tx (:writer db)]
            (d/q tx ["INSERT OR IGNORE INTO asset_bytes (sha256_hex, bytes) VALUES (?, ?)"
                     sha256-hex bytes])
            (d/q tx ["INSERT INTO asset_paths (path, stamped_path, sha256_hex) VALUES (?, ?, ?)
                     ON CONFLICT(path) DO UPDATE SET
                       stamped_path = excluded.stamped_path,
                       sha256_hex = excluded.sha256_hex"
                     path stamped-path sha256-hex]))
          stamped-path))

      (list-assets [_]
        (d/q (:reader db) ["SELECT stamped_path FROM asset_paths ORDER BY stamped_path"]))

      (push-fragments [_ _path _fragments])

      (respond [_ path]
        (when-let [bytes (first (d/q (:reader db)
                                     ["SELECT b.bytes
                                      FROM asset_paths p
                                      JOIN asset_bytes b ON b.sha256_hex = p.sha256_hex
                                      WHERE p.stamped_path = ?"
                                      path]))]
          {:body bytes})))))
