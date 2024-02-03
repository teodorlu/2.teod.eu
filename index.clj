(ns index
  {:nextjournal.clerk/visibility {:code :hide :result :hide}
   :nextjournal.clerk/open-graph
   {:url "https://2.teod.eu"}}
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
  [:h1.font-arial "hi"]
  [:p.font-arial "hi hi "]
  ;; we can still rely on Clerk data strucutres in here

  (let [xs (range 7)]
    (clerk/table
     {:xs xs
      :xs-squared (map #(* % %) xs)}))

  (let [xs (range 7)]
    (clerk/with-viewer clerk.viewer/map-viewer
      {:xs xs
       :xs-squared (map #(* % %) xs)}))

  [:br]

  (let [xs (range 7)]
    (clerk/with-viewer clerk.viewer/fallback-viewer
      {:xs xs
       :xs-squared (map #(* % %) xs)}))
  ]
 )
