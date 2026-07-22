(ns assetnow.hash-test
  (:require [assetnow.hash :as hash]
            [clojure.test :refer [deftest is]]))

(deftest bytes->md5-hexdigest-test
  (let [bytes (.getBytes "Call me Ishmael." "UTF-8")]
    (is (= "cbee6f8c54d146395a727b9097e5972a"
           (hash/bytes->md5-hexdigest bytes)))))
