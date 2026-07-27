(ns t2.deploy
  (:require [babashka.process]))

(def targets ["root@t3:/var/www/2.teod.eu/"
              "root@t3:/var/www/3.teod.eu/"])
(def include ["index.html" "d" "jtk" "css" "fonts"])
(def exclude ["*.ttext" "*.ttex" "*.htm" ".DS_Store"])

(defn deploy-argv [target]
  (into []
        cat
        [["rsync" "-a" "--delete" "--relative"]
         (map #(str "--exclude=" %) exclude)
         include
         [target]]))

(defn deploy-dry-argv [target]
  (conj (deploy-argv target) "-n" "-i"))

(defn deploy []
  (run! #(babashka.process/shell (deploy-argv %)) targets))

(defn deploy-dry []
  (run! #(babashka.process/shell (deploy-dry-argv %)) targets))

