(ns assetnow.checksum
  (:require [babashka.fs :as fs])
  (:import [java.security MessageDigest]
           [java.util HexFormat]))

(defn bytes->hexdigest [algorithm ^bytes bytes]
  (.formatHex
   (HexFormat/of)
   (.digest (MessageDigest/getInstance algorithm) bytes)))

(defn bytes->md5-hexdigest [^bytes bytes]
  (bytes->hexdigest "MD5" bytes))

(defn bytes->sha256-hexdigest [^bytes bytes]
  (bytes->hexdigest "SHA-256" bytes))

(defn stamp-path [raw-path checksum]
  (str (fs/strip-ext raw-path) "." checksum "." (fs/extension raw-path)))
