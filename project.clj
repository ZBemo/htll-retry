(defproject htll "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "0BSD"
            :url "https://spdx.org/licenses/0BSD.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :main ^:skip-aot htll.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
