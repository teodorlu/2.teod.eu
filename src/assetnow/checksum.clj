(ns assetnow.checksum
  (:import [java.security MessageDigest]
           [java.util HexFormat])
  (:require [babashka.fs :as fs]))

(defn bytes->md5-hexdigest [^bytes bytes]
  (.formatHex
   (HexFormat/of)
   (.digest (MessageDigest/getInstance "MD5") bytes)))

(defn stamp-path [raw-path checksum]
  (str (fs/strip-ext raw-path) "." checksum "." (fs/extension raw-path)))
