;; HTLL uses the clojure/edn functions to generate an s-expression tree from the input "lisp", which is then recursively parsed to html
;; 

(ns htll.core
  (:gen-class)
  (:require
   (clojure.edn)))

;;; The Main Loop - reduce and switch
;; Because clojure.edn will parse the subset of clojure we need to transpile to html to an s-expression,
;; We can treat going from htll to html as walking a tree of s-expressions and translating them to html
;; For Lists, we want to take the first argument and treat it as a function where 
;; the head of the list encloses the html representation of the rest of the list with a beginning and end tag
(defn node->string
  "Takes an s-expression or s-expression node and generates an html representation of it"
  ([node] (node->string "" node))
  ([^String current-string node]
   ;; We want to switch on the class of the node, as each different class generates a different part of the html
   (let [node-class (class node)]
     (cond
       ;; If the node is nil we've reached the end of the branch, so we return what we've generated
       (nil? node)  current-string
       ;; If the Node class  is a string we want to add it at the end of the current string
       (= node-class java.lang.String) (str current-string node)
       ;; If it's a list, we do one of two things depending on whether or not it starts with a symbol.
       (= node-class clojure.lang.PersistentList)
       (if (= (class (first node)) clojure.lang.Symbol)
         ;; If it does, we need to surround the parsed html from the rest of the list with begining and end tags
         ;; We need to pass the empty string to reduce, because if we don't it uses the first value in the list instead,
         ;; leading to us treating the tag as as part of the already-built string
         ;; You could factor out the let, but I think that would make it less readable
         (let [tag (first node)
               body (reduce node->string "" (rest node))]
           (str
                current-string "<"tag">"body"</"tag">"))
         ;; We should never reach this branch, because any symbol should have already been parsed out to a tag
         (throw (Exception. "function has been passed a none-function function")))
       ;; Any other classes are outside of the htll syntax
       :else (throw (Exception. (str "encountered unexpected node-type: " node-class)))))))

(defn -main
  "Reads the file at the path of the first argument and converts it to html"
  [& args]
  (println
   (node->string
    (clojure.edn/read-string
     (slurp (first args))))))

