(ns index
  {:nextjournal.clerk/visibility {:code :hide :result :hide}
   :nextjournal.clerk/open-graph
   {:url "https://2.teod.eu"
    :title "teod.eu"}}
  (:require
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk.viewer :as clerk.viewer]))

(comment
  ;; define stuff here
  )

(clerk/table [[1 2]
              [3 4]
              [5 6]])

(comment
  ;; finally, render html
  )

(defn h1 [& children]
  (into [:h1.font-arial children]))

(defn p [& children]
  (into [:p.font-arial] children))

{::clerk/visibility {:result :show}}
^{::clerk/width :full}
(clerk/html
 [:<>
  [:style {:type "text/css"
           ;; FIXME: this makes escaping in SSR happy
           :dangerouslySetInnerHTML {:__html
                                     "
#clerk .notebook-viewer .viewer:first-child {display: none;}
.dark-mode-toggle { display: none; }

.font-arial {
  font-family: Arial, sans-serif;
}
"}}]
  (h1 "hi")
  (p "hi hi ")
  ]
 )
