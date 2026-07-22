(ns assetnow.hash
  (:import [java.security MessageDigest]
           [java.util HexFormat]))

(defn bytes->md5-hexdigest [^bytes bytes]
  (.formatHex
   (HexFormat/of)
   (.digest (MessageDigest/getInstance "MD5") bytes)))
