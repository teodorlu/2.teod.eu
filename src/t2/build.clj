(ns t2.build
  (:require [t2.html :as h]
            [t2.html2 :as h2]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(defn render-pre [txt]
  (str "<!DOCTYPE html>\n"
       (h/html
        (h/head "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />")
        (h/body (h/pre txt)))))

(defn wrap [html-str]
  (str "<!DOCTYPE html>\n"
       (h/html
        (h/head "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />"
                "<style>
p { font-family: monospace; margin: 1lh 0; }
</style>")
        (h/body html-str))))

(def dottxt->dothtml #(str/replace % #".txt$" ".html"))
(def dottxt->slug #(str/replace % #".txt$" ""))

(defn index [sources]
  (-> {:type :fragment
       :content
       (into [{:type :paragraph
               :content [{:type :text :text "YO"}]}]
             (->> sources
                  (map (fn [s]
                         {:type :paragraph
                          :content [{:type :ilink
                                     :target (dottxt->dothtml s)}]}))))}
      h2/render))

(defn build [sources]
  (spit "index.html" (wrap (index sources)))
  (doseq [s sources]
    (spit (dottxt->dothtml s)
          (render-pre (slurp s)))))

(comment

  (spit "index.html" (wrap (index the-sources)))

  )

(defn clean [sources]
  (run! fs/delete-if-exists (map dottxt->dothtml sources)))

(def the-sources
  (sorted-set "d/10/wax-and-wane.txt"))

(comment
  (build the-sources)
  (clean the-sources)

  )
