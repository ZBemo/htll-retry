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
   (println node)
   (println current-string)
   ;; We want to switch on the class of the node, as each different class generates a different part of the html
   (let [node-class (class node)]
     (cond
       ;; If the node is nil we've reached the end of the branch, so we return what we've generated
       (nil? node) current-string
       ;; If the Node class  is a string we want to add it at the end of the current string
       (= node-class java.lang.String) (str node current-string)
       ;; If it's a list, we do one of two things depending on whether or not it starts with a symbol.
       (= node-class clojure.lang.PersistentList)
       (if (= (class (first node)) clojure.lang.Symbol)
         ;; If it does, we need to surround the parsed html from the rest of the list with beginnig and end tags
         (let [tag (str (first node))]
           ; (println (str "found tag: " tag))
           ; (println (rest node))
           (str current-string
                "<" tag ">"
                (reduce node->string (rest node))
                "</" tag ">"))
         ;; If it doesn't, we throw an exception, as it's not formed correctly 
         (str current-string (reduce node->string node)))
       :else (throw (Exception. (str "encountered unexpected node-type: " node-class)))))))

(defn -main
  "Reads the file at the path of the first argument and converts it to html"
  [& args]
  (println
   (node->string
    (clojure.edn/read-string
     (slurp (or (first args) "example-input.edn"))))))

