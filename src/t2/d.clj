(ns t2.d
  "document operations"
  (:refer-clojure :exclude [load find])
  (:require [babashka.fs :as fs]))

(def root "d")
(def one #(when (= 1 (count %)) (first %)))
(defn find [id]
  (some-> (fs/path root id) (fs/glob "*.ttext") one fs/file))

(def load #(some-> % find slurp))

(defn load+ [id]
  (when-let [f (find id)]
    [id (slurp f)]))

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
