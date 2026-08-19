(ns t2.manifest
  (:require [babashka.fs :as fs]
            [clojure.set :as set]))

(defn strglob [root pattern]
  (into #{} (map str) (fs/glob root pattern)))

(defn manifest []
  (into (sorted-set)
        (set/union
         #{"index.html"}
         (strglob "css" "*")
         (strglob "d" "**/*.html")
         (strglob "fonts" "*")
         (strglob "js" "*.mjs")
         (strglob "components" "*.mjs")
         #{"jtk/index.html" "jtk/jtk.css"})))
