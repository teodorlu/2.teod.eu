(ns index
  {:nextjournal.clerk/visibility {:code :hide :result :hide}
   :nextjournal.clerk/open-graph
   {:url "https://2.teod.eu"
    :title "teod.eu"}}
  (:require
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk.viewer :as clerk.viewer]
   [clojure.string :as str]))

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

(defn cleanlink [link]
  (let [clean (-> link
                  (str/replace #"^https://" "")
                  (str/replace #"^http://" "")
                  (str/replace #"^www" ""))]
    [:a {:href link} clean]))

(defn twitterlink [username]
  (let [link (str "https://twitter.com/" username)]
    [:a {:href link} (str "@" username)]))

(defn githublink [username]
  (let [link (str "https://github.com/" username)]
    [:a {:href link} (str "@" username)]))

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
  (h1 "Where is Teodor?")
  (p "Hello!")
  (p "I mess around on " (cleanlink "https://play.teod.eu") ".")
  (p (cleanlink "https://www.teodorheggelund.com")
     " and " (cleanlink "https://www.teodorheggelund.no")
     " are less messy.")
  (p "I am " (twitterlink "teodorlu") " on Twitter, and "
     (githublink "teodorlu") " on Github.")
  (p "Have a nice day!"
     [:br]
     "Teodor")])
