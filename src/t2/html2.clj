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

    :blockquote
    (do (.append sb "\n<blockquote><p>")
        (run! (partial render* sb) (:content ir))
        (.append sb "</p></blockquote>"))

    :fragment
    (run! (partial render* sb)
          (:content ir))

    :ilink
    (do
      (doto sb
        (.append "<a href=\"")
        (.append (:target ir))
        (.append "\">"))
      (if (:content ir)
        (run! (partial render* sb) (:content ir))
        (.append sb (:target ir)))
      (.append sb "</a>"))

    :link
    (do
      (assert (get-in ir [:attrs :href]))
      (doto sb
        (.append "<a href=\"")
        (.append (get-in ir [:attrs :href]))
        (.append "\">"))
      (run! (partial render* sb) (:content ir))
      (.append sb "</a>"))

    :separator
    (.append sb "<div style=\"height: 1px\"></div>")

    :strong
    (do (.append sb "<strong>")
        (run! (partial render* sb) (:content ir))
        (.append sb "</strong>"))

    :em
    (do (.append sb "<em>")
        (run! (partial render* sb) (:content ir))
        (.append sb "</em>"))

    :softbreak
    (.append sb " "))
  sb)

(defn render [ir]
  (str (doto (StringBuilder.) (render* ir)) "\n"))

(comment

  (render
   {:type :fragment
    :content [{:type :paragraph :content [{:type :text :text "YO"}]}
              {:type :paragraph :content [{:type :text :text "WAX AND WANE"}]}]})
  ;; => "\n<p>YO</p>\n<p>WAX AND WANE</p>\n"

  (render
   {:type :ilink :target "/d/10/wax-and-wane.html"})
  ;; => "<a href=\"/d/10/wax-and-wane.html\">/d/10/wax-and-wane.html</a>\n"

  )
