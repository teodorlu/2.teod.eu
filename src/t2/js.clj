(ns t2.js
  "Take Clojure data, make JavaScript source"
  (:require [clojure.string :as str]))

(defn string [s]
  (str \" (-> s (str/replace "\\" "\\\\") (str/replace "\"" "\\\"")) \"))

(defn value [v]
  (cond
    (string? v)     (string v)
    (keyword? v)    (string (name v))
    (number? v)     (str v)
    (boolean? v)    (str v)
    (nil? v)        "null"
    (map? v)        (str "{" (str/join ", " (map (fn [[k x]] (str (value k) ": " (value x))) v)) "}")
    (sequential? v) (str "[" (str/join ", " (map value v)) "]")
    :else           (throw (ex-info "unsupported value" {:value v}))))

(defn export-const [nm m]
  (str "export const " nm " = {\n"
       (str/join (for [[k v] m] (str "  " (value k) ": " (value v) ",\n")))
       "};\n"))

(comment
  (value {:href "/d/26/spectacle-spetakkel.html"})
  (export-const "d" (sorted-map "26" {:href "/a" :text "a"}))
  )
