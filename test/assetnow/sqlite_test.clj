(ns assetnow.sqlite-test
  (:require [assetnow.protocols :as p]
            [assetnow.sqlite :as sqlite]
            [clojure.test :refer [deftest is]])
  (:import (java.util Arrays)))

(set! *warn-on-reflection* true)

(defonce store
  (doto (sqlite/create)
    (p/push-asset "/moby-dick.txt" (.getBytes "Call me Ishmael." "UTF-8"))))

(deftest push-list-respond
  (is (= "/moby-dick.458f3ceeeec730139693560ecf66c9c22d9c7bc7dcb0599e8e10b667dfeac043.txt"
         (p/push-asset store "/moby-dick.txt" (.getBytes "Call me Ishmael." "UTF-8"))))

  (is (some #{"/moby-dick.458f3ceeeec730139693560ecf66c9c22d9c7bc7dcb0599e8e10b667dfeac043.txt"}
            (p/list-assets store)))

  (is (^[bytes bytes] Arrays/equals
       (.getBytes "Call me Ishmael." "UTF-8")
       (:body (p/respond store "/moby-dick.458f3ceeeec730139693560ecf66c9c22d9c7bc7dcb0599e8e10b667dfeac043.txt"))))

  (is (nil? (p/respond store "/nope.txt"))))
