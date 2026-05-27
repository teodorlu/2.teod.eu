(ns t2.build
  (:require [t2.html :as h]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(defn render-pre [txt]
  (str "<!DOCTYPE html>\n"
       (h/html (h/body (h/pre txt)))))

(def dottxt->dothtml #(str/replace % #".txt$" ".html"))
(def dottxt->slug #(str/replace % #".txt$" ""))

(defn index [sources]
  (str
   "YO

READ STUFF:\n"
   (->> sources
        (map #(str "  " (dottxt->slug %)))
        (str/join "\n"))))

(defn build [sources]
  (spit "index.html" (render-pre (index sources)))
  (doseq [s sources]
    (spit (dottxt->dothtml s)
          (render-pre (slurp s)))))

(comment

  (spit "index.html" (render-pre (index the-sources)))

  )

(defn clean [sources]
  (run! fs/delete-if-exists (map dottxt->dothtml sources)))

(def the-sources
  (sorted-set "d/10/wax-and-wane.txt"))

(comment
  (build the-sources)
  (clean the-sources)

  )
