(ns htll.core
  (:gen-class)
  (:require
   (clojure.edn)))

; TODO: we forgot to handle persistent-lists lol
(defn node->string [node]
  (let [node-class (class node)
        list->string (fn [node built-string]
                       (str built-string (node->string node)))]
    (cond
      (= node-class java.lang.String) (do (println "string") node)
      (= node-class clojure.lang.Symbol)
      (do (println "symbol")
          (let
           [tag-name (str first node)]
            (str "<" tag-name ">")))
      (= node-class clojure.lang.PersistentList)
      (do (println "List")
          (reduce list->string node))
      :else (println "unexpected")
      #_(str "UNEXPECTED CLASS \"" node-class \"))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  ; slurp in the (hardcoded for now) input file and parse it to an ast
  (println
   (node->string
    (clojure.edn/read-string
     (slurp "example-input.edn")))))
