(ns t2.html
  "Hvorfor gjøre det vanskelig og kjipt når man kan gjøre det som Jonas?"
  (:require [clojure.string :as str]))

(def open #(str "<" % ">"))
(def close #(str "</" % ">"))

(defn el [tag & children] (str (open tag) (str/join "\n" children) (close tag)))

(def html (partial el "html"))
(def body (partial el "body"))
(def pre (partial el "pre"))
