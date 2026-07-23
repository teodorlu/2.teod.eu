(ns assetnow.checksum-test
  (:require [assetnow.checksum :as checksum]
            [clojure.test :refer [deftest is]]))

(deftest bytes->md5-hexdigest-test
  (let [bytes (.getBytes "Call me Ishmael." "UTF-8")]
    (is (= "cbee6f8c54d146395a727b9097e5972a"
            (checksum/bytes->md5-hexdigest bytes)))))

(deftest bytes->sha256-hexdigest-test
  (let [bytes (.getBytes "Call me Ishmael." "UTF-8")]
    (is (= "458f3ceeeec730139693560ecf66c9c22d9c7bc7dcb0599e8e10b667dfeac043"
           (checksum/bytes->sha256-hexdigest bytes)))))

(deftest stamp-path
  (is (= "/moby-dick.cbee.txt"
         (checksum/stamp-path "/moby-dick.txt" "cbee"))))
