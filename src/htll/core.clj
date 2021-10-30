;;; We want to reteurn a function that takes the last generated string and concatenates it inside of the next generated string
;;; This allows us to return (fn [gen-string] (str "<" *tag-name* ">" gen-string "</" *tag-name* ">"))
;;; which is then called on whatever goes inside 

(ns htll.core
  (:gen-class)
  (:require
   (clojure.edn)))

; TODO: we forgot to handle persistent-lists lol
; TODO: This should just do the reduce itself so we can use recur and avoid a stack overflow 
;;;; The Main loop - reduce and switch
;; Because clojure.edn will parse the subset of clojure we need to transpile to html to an s-expression,
;; We can treat going from htll to html as recursively walking a list.
;; I chose to re-implement reduce-like functionallity myself in order to afford more control over when and how we recurse.
(defn node->string
  ;; The function is multi-arrity so that the callee has a better api
  ([node] ((node->string node "")))
  ;; This is the actual meat of the function, where we recursively walk the s-expression and convert it to html
  ([node ^String string]
   (let [node-class (class node)]
     ;; We generate html from the node differently based on what class it is, 
     (cond
       ;; If it's nill that means we've reached the end of this branch, so we just return the generated string and let the function above us deal with it
       (= node-class nil) (fn [gen-string] gen-string)
       ;; We follow the same reasoning as nil if it's a string, except we need to add it at the end of the branch as well
       (= node-class java.lang.String) (fn [gen-string] (str gen-string node))
       ;;TODO: return a function
       ;; if it's a symbol we want to generate a string equivalent to <*symbol*> *generated-text* </*symbol*> where *...* means to insert it into the string
        ;; so we return a function that takes the string generated from that branch and inserts it in between "<*symbol*>..."
       (= node-class clojure.lang.Symbol)
       (let [node (str node)]
         (fn [gen-string] (str "<" node ">" gen-string "</" node ">")))
       ;TODO: figure this out
       (= node-class clojure.lang.PersistentList)
       (let [tag (first node)])))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println
   (node->string
    (clojure.edn/read-string
     (slurp (or (first args) "example-input.edn"))))))

