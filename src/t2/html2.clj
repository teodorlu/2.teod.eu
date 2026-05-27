(ns t2.html2
  "Take Nexjournal Markdown IR, make HTML strings

  Nextjournal Markdown: https://nextjournal.github.io/markdown/README/"
  (:import (java.lang StringBuilder)))

(set! *warn-on-reflection* true)

(defn render* [^StringBuilder sb ir]
  (case (:type ir)
    :text
    (.append sb (:text ir))

    :paragraph
    (do (.append sb "\n<p>")
        (run! (partial render* sb) (:content ir))
        (.append sb "</p>"))

    :fragment
    (run! (partial render* sb)
          (:content ir))

    :ilink
    (doto sb
      (.append "<a href=\"")
      (.append (:target ir))
      (.append "\">")
      (.append (:target ir))
      (.append "</a>")))
  sb)

(defn render [ir]
  (str (doto (StringBuilder.) (render* ir))))

(comment

  (render
   {:type :fragment
    :content [{:type :paragraph :content [{:type :text :text "YO"}]}
              {:type :paragraph :content [{:type :text :text "WAX AND WANE"}]}]})
  ;; => "<p>YO</p><p>d/10/wax-and-wane</p>"

  (render
   {:type :ilink :target "/d/10/wax-and-wane.html"})
  ;; => "<a href=\"/d/10/wax-and-wane.html\">/d/10/wax-and-wane.html</a>"

  )
