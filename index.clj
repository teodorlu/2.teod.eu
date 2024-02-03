(ns index
  {:nextjournal.clerk/visibility {:code :hide :result :hide}
   :nextjournal.clerk/open-graph
   {:url "https://2.teod.eu"}}
  (:require
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk.view :as clerk.view]))

(alter-var-root #'clerk.view/include-css+js
                (fn [include-css+js-orig extra-includes]
                  (fn [state]
                    (concat (include-css+js-orig state)
                            extra-includes)))
                (list))


2

3

:hello/world

:trigger/rebuild

(clerk/table [[1 2]
              [3 4]
              [5 6]])

{::clerk/visibility {:result :show}}
^{::clerk/width :full}
(clerk/html
 [:<>
  [:style {:type "text/css"
           ;; FIXME: this makes escaping in SSR happy
           :dangerouslySetInnerHTML {:__html
                                     "
#clerk .notebook-viewer .viewer:first-child {
  display: none;
}
"}}]
  [:h1 "hi"]
  [:p "hi hi "]])
