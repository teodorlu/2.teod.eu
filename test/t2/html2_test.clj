(ns t2.html2-test
  (:require [t2.html2]
            [clojure.test :refer [deftest is testing]]))

(deftest ilink
  (testing "sane default linktext"
    (is (= "<a href=\"/\">/</a>\n"
           (t2.html2/render {:type :ilink :target "/"}))))

  (testing "that can be overridden"
    (is (= "<a href=\"/\">↰</a>\n"
           (t2.html2/render {:type :ilink
                             :target "/"
                             :content [{:type :text
                                        :text "↰"}]})))))
