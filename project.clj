(defproject japan-sat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]
                 [org.clojure/spec.alpha "0.1.143"]
                 [org.clojure/data.json "0.2.6"]
                 [rolling-stones "1.0.0-SNAPSHOT"]]
  :main ^:skip-aot japan-sat.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
