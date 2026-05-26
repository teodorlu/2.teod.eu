(ns t2.build)

(comment
  (require '[t2.html :refer [html body pre]])
  (def text (slurp "d/10/wax-and-wane.txt"))
  (html (body (pre text)))

  (spit "d/10/wax-and-wane.html"
        (str
         "<!DOCTYPE html>\n"
         (html (body (pre text)))))

  )
