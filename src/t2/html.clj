(ns t2.html
  "Hvorfor gjøre det vanskelig og kjipt når man kan gjøre det som Jonas?"
  (:require [clojure.string :as str]))

(def open #(str "<" % ">"))
(def close #(str "</" % ">"))

(defn el [tag]
  (fn [& children]
    (str (open tag) (str/join "\n" children) (close tag))))

(def html (el "html"))
(def head (el "head"))
(def body (el "body"))
(def pre (el "pre"))
