(ns t2.d
  "document operations"
  (:refer-clojure :exclude [load find next])
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

(def next #(-> % (Long/parseLong 16) inc (Long/toString 16)))

(comment
  (all)
  (find "10")
  (load "10")
  (load+ "10")

  (next "19")

  ;; Finn slugs/filnavn
  (into (sorted-set)
        (map (comp fs/file-name find))
        (all))

  )
