(ns t2.d
  "document operations"
  (:refer-clojure :exclude [load find])
  (:require [babashka.fs :as fs]))

(def root "d")
(def one #(when (= 1 (count %)) (first %)))
(def find #(some-> (fs/path root %) (fs/glob "*.ttext") one fs/file))

(def load #(some-> % find slurp))

(defn load+ [id]
  (when-let [f (find id)]
    [id (slurp f)]))

(->> (fs/list-dir root)
     (map fs/file-name)
     (filter find)
     )

(defn all []
  (into (sorted-set)
        (comp (map fs/file-name)
              (filter find))
        (fs/list-dir root)))

(comment
  (all)
  (find "10")
  (load "10")
  (load+ "10")
  )
