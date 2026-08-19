(ns t2.manifest-test
  (:require [clojure.set :as set]
            [clojure.test :refer [deftest is]]
            [t2.manifest :as manifest]))

(deftest manifest
  (is
   (set/superset? (manifest/manifest)
                  #{"index.html"
                    "css/t2.css"
                    "d/10/wax-and-wane.html"
                    "fonts/nunito-latin.woff2"
                    "js/index.mjs"
                    "jtk/index.html"
                    "jtk/jtk.css"})))
