(ns t2.build
  (:require [t2.html :as h]
            [t2.html2 :as h2]
            [t2.ttext]
            [clojure.string :as str]
            [babashka.fs :as fs]))

(defn wrap [html-str]
  (str "<!DOCTYPE html>\n"
       (h/html
        (h/head "<meta charset=\"utf-8\" />"
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />"
                "<style>
p { font-family: monospace; margin: 1lh 0; }
</style>"
                "\n")
        (h/body html-str))))

(def dot-ttext->dot-html #(str/replace % #".ttext$" ".html"))

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
          (-> s slurp t2.ttext/parse t2.html2/render wrap))))

(comment

  (spit "index.html" (wrap (index the-sources)))

  )

(defn clean [sources]
  (run! fs/delete-if-exists (map dot-ttext->dot-html sources)))

(def the-sources
  (sorted-set "d/10/wax-and-wane.ttext"))

(comment
  (build the-sources)
  (clean the-sources)

  )
