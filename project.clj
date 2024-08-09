(defproject io.github.mbroughani81/openwhisk-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.google.code.gson/gson "2.11.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [mysql/mysql-connector-java "8.0.33"]
                 [cheshire "5.13.0"]
                 ;;
                 ]
  :main ^:skip-aot io.github.mbroughani81.openwhisk-app.Main
  :repl-options {:init-ns io.github.mbroughani81.openwhisk-app}
  :profiles {:uberjar {:aot :all}})
