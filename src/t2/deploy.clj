(ns t2.deploy
  (:require [babashka.process]))

(def target "root@t3:/var/www/2.teod.eu/")
(def include ["components" "css" "d" "fonts" "index.html" "jtk"])
(def exclude ["*.htm" "*.ttex" "*.ttext" ".DS_Store"])

(defn deploy-argv []
  (into []
        cat
        [["rsync" "-a" "--delete" "--relative"]
         (map #(str "--exclude=" %) exclude)
         include
         [target]]))

(defn deploy-dry-argv []
  (conj (deploy-argv) "-n" "-i"))

(defn deploy []
  (babashka.process/shell (deploy-argv)))

(defn deploy-dry []
  (babashka.process/shell (deploy-dry-argv)))

