(ns assetnow.mem-test
  (:require [assetnow.mem :as mem]
            [assetnow.protocols :as p]
            [clojure.test :refer [deftest is]])
  (:import (java.util Arrays)))

(set! *warn-on-reflection* true)

(def store (mem/create))

(deftest push-list
  (is (= "/moby-dick.cbee6f8c54d146395a727b9097e5972a.txt"
         (p/push-asset store "/moby-dick.txt"
                       (String/.getBytes "Call me Ishmael." "UTF-8"))))

  (is (some #{"/moby-dick.cbee6f8c54d146395a727b9097e5972a.txt"}
            (p/list-assets store)))

  (is (^[bytes bytes] Arrays/equals
       (String/.getBytes "Call me Ishmael." "UTF-8")
       (-> (p/respond store "/moby-dick.cbee6f8c54d146395a727b9097e5972a.txt")
           :body))))

(comment
  (p/push-asset store "/moby-dick.txt" (String/.getBytes "Call me Ishmael." "UTF-8"))

  (p/push-fragments
   store "/text.html"
   [(String/.getBytes "You'll find Moby Dick at <a href=\"")
    "/moby-dick.txt"
    "\">moby-dick.txt</a>."])

  (p/respond store "/text.html")

  (p/list-assets store)
  (p/respond store "/moby-dick.cbee6f8c54d146395a727b9097e5972a.txt")
  )
