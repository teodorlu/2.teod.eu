(ns t2.build
  (:require [babashka.fs :as fs]
            [clojure.string :as str]
            [t2.d :as d]
            [t2.html :as h]
            [t2.html2 :as h2]
            [t2.ttext]))

(defn wrap [html-str]
  (str "<!DOCTYPE html>\n"
       (h/html
        (h/head "<meta charset=\"utf-8\" />"
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />"
                "<style>
p { font-family: monospace; margin: 1lh 0; }
</style>"
                "\n")
        (h/body html-str))
       "\n"))

(defn dot-ttext->dot-html [s]
  (-> s
      (str/replace #".ttext$" ".html")
      (str/replace #".ttex$" ".html")))

(defn index [sources]
  (-> {:type :fragment
       :content
       (into [{:type :paragraph
               :content [{:type :text :text "YO"}]}]
             (->> sources
                  (map (fn [s]
                         {:type :paragraph
                          :content [{:type :ilink
                                     :target (dot-ttext->dot-html s)}]}))))}
      h2/render))

(defn build [sources]
  (spit "index.html" (wrap (index sources)))
  (doseq [s sources]
    (spit (dot-ttext->dot-html s)
          (-> {:type :fragment
               :content [{:type :ilink
                          :target "/"
                          :content [{:type :text
                                     :text "↰"}]}
                         (-> s slurp t2.ttext/parse)]}

              t2.html2/render wrap))))

(defn clean [sources]
  (run! fs/delete-if-exists (map dot-ttext->dot-html sources)))

(def drafts #{"17"})

(def the-sources
  (into (sorted-set)
        (comp (remove drafts)
              (map (comp str d/find)))
        (d/all)))

(comment
  (build the-sources)
  (clean the-sources)

  )
