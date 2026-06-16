(ns t2.ttext
  (:require [clojure.string :as str]
            [nextjournal.markdown]
            [t2.d :as d]))

(def parse-paragraph
  (comp first :content nextjournal.markdown/parse))

(defn parse [s]
  {:type :fragment
   :content
   (->> (str/split s #"\n\n\n")
        (interpose {:type :separator})
        (mapcat (fn [x]
                  (if-not (string? x)
                    [x]
                    (str/split x #"\n\n"))))
        (map (fn [x]
               (if-not (string? x)
                 x
                 (parse-paragraph x)))))})

(comment
  (parse (d/load "10"))

  (require 't2.html2)
  (-> "10" d/load parse t2.html2/render)
  (-> "15" d/load parse t2.html2/render)
  )
