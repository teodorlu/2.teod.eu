(ns t2.js-test
  (:require [clojure.test :refer [deftest is testing]]
            [t2.js :as js]))

(deftest string
  (is (= "\"a\"" (js/string "a")))
  (is (= "\"say \\\"hi\\\"\"" (js/string "say \"hi\"")))
  (is (= "\"c:\\\\tmp\"" (js/string "c:\\tmp")))
  (is (= "\"nåde\"" (js/string "nåde"))))

(deftest value
  (testing "scalars"
    (is (= "\"a\"" (js/value "a")))
    (is (= "\"href\"" (js/value :href)))
    (is (= "1" (js/value 1)))
    (is (= "true" (js/value true)))
    (is (= "false" (js/value false)))
    (is (= "null" (js/value nil))))

  (testing "maps"
    (is (= "{}" (js/value {})))
    (is (= "{\"href\": \"/a\"}" (js/value {:href "/a"})))
    (is (= "{\"href\": \"/a\", \"draft\": true}"
           (js/value {:href "/a" :draft true}))))

  (testing "sequences"
    (is (= "[]" (js/value [])))
    (is (= "[\"a\", \"b\"]" (js/value ["a" "b"]))))

  (testing "unsupported"
    (is (thrown? clojure.lang.ExceptionInfo (js/value #{"a"})))))

(deftest export-const
  (is (= "export const d = {\n};\n"
         (js/export-const "d" (sorted-map))))
  (is (= (str "export const d = {\n"
              "  \"26\": {\"href\": \"/d/26/x.html\", \"text\": \"d/26/x.html\"},\n"
              "};\n")
         (js/export-const "d" (sorted-map "26" {:href "/d/26/x.html"
                                                :text "d/26/x.html"})))))
