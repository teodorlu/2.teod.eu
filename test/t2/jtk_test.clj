(ns t2.jtk-test
  (:require
   [clojure.test :refer [deftest is]]
   [t2.jtk :as jtk]))

(set! *print-namespace-maps* false)

(deftest parse-ttext
  (is (= (-> "2
2026-07-10
Julian

Altså, hvilke valg kan vi ta?"
             jtk/parse-ttext
             (select-keys [:jtk/title :jtk/date :jtk/author]))
         {:jtk/title "2"
          :jtk/date "2026-07-10"
          :jtk/author "Julian"}))
  )

(deftest parse-htm
  (is (= (-> "<jtk-title>2</jtk-title>
<jtk-date>2026-07-10</jtk-date>
<jtk-author>Julian</jtk-author>

<p>Altså, hvilke valg kan vi ta? "
             jtk/parse-htm
             (select-keys [:jtk/title :jtk/date :jtk/author]))
         {:jtk/title "2"
          :jtk/date "2026-07-10"
          :jtk/author "Julian"})))

