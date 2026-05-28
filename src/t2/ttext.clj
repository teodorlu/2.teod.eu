(ns t2.ttext
  (:require [clojure.string :as str]
            [nextjournal.markdown]))

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
  (-> (slurp "d/10/wax-and-wane.ttext")
      parse)

  (require 't2.html2)
  (-> (slurp "d/10/wax-and-wane.ttext")
      parse
      t2.html2/render)

  )
