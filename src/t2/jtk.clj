(ns t2.jtk
  {:llm-generated "2026-07-15"}
  (:require [babashka.fs :as fs]
            [clojure.string :as str]
            [t2.html2 :as html2]
            [t2.ttext :as ttext]))

(def root "jtk")

(defn entry-file? [path]
  (and (fs/regular-file? path)
       (= "ttext" (fs/extension path))))

(defn all []
  (->> (fs/list-dir root)
       (filter fs/directory?)
       (mapcat #(fs/glob % "*.ttext"))
       (filter entry-file?)
       sort))

(defn parse [s]
  (let [[title date author & body-lines] (str/split-lines s)]
    {:title title
     :date date
     :author author
     :content (->> body-lines
                   (str/join "\n")
                   str/trim
                   (#(str/split % #"\n\n"))
                   (remove str/blank?)
                   (map ttext/parse-paragraph))}))

(defn render-entry [{:keys [title date author content]}]
  (str "\n<article>"
       "\n<header>"
       "\n<strong>" title "</strong>"
       "\n<span>" date "</span>"
       "\n<span>" author "</span>"
       "\n</header>"
       "\n<section>"
       (html2/render {:type :fragment :content content})
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
        (->> (all)
             (map (comp parse slurp str))
             index
             wrap)))

(comment
  (build)
  )
