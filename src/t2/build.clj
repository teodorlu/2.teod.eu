(ns t2.build
  (:require
   [babashka.fs :as fs]
   [clojure.set :as set]
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
      (str/replace #".ttex$" ".html")
      (str/replace #".htm$" ".html")))

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

(defn build-ttext [ttext-str]
  (-> {:type :fragment
       :content [{:type :ilink
                  :target "/"
                  :content [{:type :text
                             :text "↰"}]}
                 (t2.ttext/parse ttext-str)]}
      t2.html2/render wrap))

(defn build-htm [htm-str]
  (wrap (str "<p><a href=\"/\">↰</a></p>\n" htm-str)))

(def ext->build-fn
  {"ttext" #'build-ttext
   "htm" #'build-htm})

(defn build [published-ids draft-ids]
  (let [published (into (sorted-set) (map (comp str d/find)) published-ids)
        drafts (into (sorted-set) (map (comp str d/find)) draft-ids)]
    (spit "index.html" (wrap (index published)))
    (doseq [s (set/union published drafts)]
      (spit (dot-ttext->dot-html s)
            ((or (ext->build-fn (fs/extension s))
                 (throw (ex-info "unsupported extension"
                                 {:extension (fs/extension s)
                                  :source s})))
             (slurp s))))))

(defn clean [source-ids]
  (let [sources (into #{} (map (comp str d/find)) source-ids)]
    (run! fs/delete-if-exists (map dot-ttext->dot-html sources))))

(def draft-ids #{"19" "1c" "20"})
(def all-ids (set (d/all)))
(def published-ids (set/difference all-ids draft-ids))

(comment
  (set! *print-namespace-maps* false)

  ;; build
  (build published-ids draft-ids)
  (clean all-ids)

  ;; d/...
  (def all (d/all))
  (d/find (first all))
  (-> all last d/find slurp t2.ttext/parse)

  ;; render
  (t2.html2/render {:type :link,
                    :attrs {:href "https://play.teod.eu/simple-made-easy"},
                    :content [{:type :text, :text "play.teod.eu/simple-made-easy"}]})

  ;; jtk
  (spit "jtk/01/julian-og-teodor-konverserer.html"
        (-> (slurp "jtk/01/julian-og-teodor-konverserer.ttext")
            t2.ttext/parse
            t2.html2/render
            wrap))

  (spit "jtk/02/2.html"
        (-> (slurp "jtk/02/2.ttext")
            t2.ttext/parse
            t2.html2/render
            wrap))

  )
