(ns t2.references
  "Parse references as text to map from label to location

  Text:

    \\references
    [marsfart] /d/1b/marsfart
    [en god natts søvn] https://mikrobloggeriet.no/olorm/olorm-1

  Map:

    {\"en god natts søvn\" \"https://mikrobloggeriet.no/olorm/olorm-1\"
     \"marsfart\" \"/d/1b/marsfart\"}"
  (:require [clojure.string :as str]))

(def line-regex #"^\[([^\[\]\r\n]+)\][^\S\r\n]+([^\r\n]+)$")

(defn parse-line [line]
  (when-let [[_ identifier location] (re-matches line-regex line)]
    [identifier location]))

(defn parse-str [s]
  (let [[l1 & rest] (str/split-lines s)]
    (when (= "\\references" (str/trim l1))
      (into (sorted-map) (keep parse-line) rest))))

(defn write-str [references]
  (let [sb (StringBuilder.)]
    (StringBuilder/.append sb "\\references\n")
    (doseq [[label location] references]
      (doto sb
        (StringBuilder/.append "[")
        (StringBuilder/.append label)
        (StringBuilder/.append "] ")
        (StringBuilder/.append location)
        (StringBuilder/.append "\n")))
    (str sb)))

(comment
  (set! *warn-on-reflection* true)
  )
