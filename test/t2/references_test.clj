(ns t2.references-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [t2.references :as references]))

(deftest parse-line
  (is (nil? (references/parse-line "[]")))
  (is (nil? (references/parse-line "[marsfart /d/1b/marsfart")))
  (is (nil? (references/parse-line "marsfart /d/1b/marsfart")))
  (is (= ["marsfart" "/d/1b/marsfart"]
         (references/parse-line "[marsfart] /d/1b/marsfart"))))

(defn by-lines [& lines]
  (str/join "\n" lines))

(deftest parse-str
  (testing "leads with \\references"
    (is (nil? (references/parse-str
               (by-lines "[marsfart] /d/1b/marsfart")))))

  (testing "parses"
    (is (= {"marsfart" "/d/1b/marsfart"}
           (references/parse-str
            "\\references
[marsfart] /d/1b/marsfart")))
    (is (= {"en god natts søvn" "https://mikrobloggeriet.no/olorm/olorm-1"
            "marsfart" "/d/1b/marsfart"}
           (references/parse-str "\\references
[en god natts søvn] https://mikrobloggeriet.no/olorm/olorm-1
[marsfart] /d/1b/marsfart")))))

(deftest write-str
  (is (= (str "\\references\n"
              "[marsfart] /d/1b/marsfart\n")
         (references/write-str (sorted-map "marsfart" "/d/1b/marsfart")))))
