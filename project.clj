(defproject io.github.mbroughani81/openwhisk-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 ;; gson
                 [com.google.code.gson/gson "2.11.0"]
                 ;; db
                 [org.clojure/java.jdbc "0.7.12"]
                 [com.mchange/c3p0 "0.10.1"]
                 [mysql/mysql-connector-java "8.0.33"]
                 [org.postgresql/postgresql "42.7.3"]
                 ;; parsing
                 [cheshire "5.13.0"]
                 ;; component
                 [com.stuartsierra/component "1.1.0"]
                 ;; hashing
                 [buddy/buddy-hashers "2.0.167"]
                 ;; http
                 [clj-http "3.13.0"]
                 ;;
                 ]
  :main ^:skip-aot io.github.mbroughani81.openwhisk-app.Main
  :repl-options {:init-ns io.github.mbroughani81.openwhisk-app}
  :profiles {:uberjar {:aot :all}})
