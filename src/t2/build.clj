(ns t2.build
  (:require [t2.html :as h]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(def dottxt->dothtml #(str/replace % #".txt$" ".html"))

(defn txt->html [txt]
  (str "<!DOCTYPE html>\n"
       (h/html (h/body (h/pre txt)))))

(defn build [sources]
  (doseq [s sources]
    (spit (dottxt->dothtml s)
          (txt->html (slurp s)))))

(defn clean [sources]
  (run! fs/delete-if-exists (map dottxt->dothtml sources)))

(def the-sources
  (sorted-set "d/10/wax-and-wane.txt"))

(comment
  (build the-sources)
  (clean the-sources)

  )
