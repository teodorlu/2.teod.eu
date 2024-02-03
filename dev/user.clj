(ns user
  (:require
   [nextjournal.clerk :as clerk]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn clerk-start! []
  (let [clerk-serve (requiring-resolve 'nextjournal.clerk/serve!)
        clerk-port 7919]
    (clerk-serve {:browse? true :port clerk-port})))

(comment
  (clerk-start!)
  (clerk/halt!))
