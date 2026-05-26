(ns t2.build)

(comment
  (require '[t2.html :refer [html body pre]])
  (spit "d/10/wax-and-wane.html"
        (str
         "<!DOCTYPE html>\n"
         (html (body (pre (slurp "d/10/wax-and-wane.txt"))))))

  )
