(ns t2.jtk
  {:llm-generated "2026-07-15"
   :tedor-edited "2026-07-19"}
  (:require
   [babashka.fs :as fs]
   [clojure.spec.alpha :as s]
   [clojure.string :as str]
   [hickory.core :as hickory]
   [t2.html2 :as html2]
   [t2.ttext :as ttext])
  (:import
   (org.jsoup.nodes Element)))

;; Nextjournal markdown sequence of AST nodes
(s/def :content/nxmd seqable?)

;; HTML string
(s/def :content/htm string?)

(def root "jtk")

(def entry-globexpr "[0-9a-f][0-9a-f]*-*.{htm,ttext}")
(def entry-globexpr-old "[0-9a-f][0-9a-f]*-*.ttext")

(defn parse-ttext [ttext-str]
  (let [[title date author & body-lines] (str/split-lines ttext-str)]
    {:jtk/title title
     :jtk/date date
     :jtk/author author
     :content/nxmd (->> body-lines
                        (str/join "\n")
                        str/trim
                        (#(str/split % #"\n\n"))
                        (remove str/blank?)
                        (map ttext/parse-paragraph))}))

(defn select-tag-text
  [jsoup-root tag]
  (some-> jsoup-root
          (Element/.select tag)
          first
          Element/.text))

(defn kw->tag [kw]
  (if-let [n (namespace kw)]
    (str n "-" (name kw))
    (name kw)))

(defn parse-htm [html-str]
  (let [jsoup-root (hickory/parse html-str)]
    (-> (reduce (fn [m kw]
                  (if-let [text (select-tag-text jsoup-root (kw->tag kw))]
                    (assoc m kw text)
                    m))
                {}
                [:jtk/title :jtk/date :jtk/author])
        (assoc :content/htm html-str))))

(defn render-entry [entry]
  (str "\n<article>"
       "\n<header>"
       "\n<strong>" (:jtk/title entry) "</strong>"
       "\n<span>" (:jtk/date entry) "</span>"
       "\n<span>" (:jtk/author entry) "</span>"
       "\n</header>"
       "\n<section>"
       (html2/render {:type :fragment :content (:content/nxmd entry)})
       "</section>"
       "\n</article>"))

(defn wrap [html-str]
  (str "<!DOCTYPE html>\n"
       "<html><head><meta charset=\"utf-8\" />\n"
       "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n"
       "<link rel=\"stylesheet\" href=\"jtk.css\" />\n\n"
       "</head>\n"
       "<body>\n"
       html-str
       "\n</body></html>\n"))

(defn index [entries]
  (str "<main>"
       (apply str (map render-entry entries))
       "\n</main>"))

(defn build []
  (spit (str (fs/path root "index.html"))
        (->> (fs/glob root entry-globexpr-old)
             (map (comp parse-ttext slurp str))
             index
             wrap)))

(comment
  (build)
  )
