(ns io.github.mbroughani81.db
  (:require [com.stuartsierra.component :as component]
            [clojure.java.jdbc :as jdbc])
  (:import (com.mchange.v2.c3p0 ComboPooledDataSource DataSources)))

;; ------------------------------------------------------------ ;;

(def db-spec
  {:classname   "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname     "//localhost:3306/openwhisk_app_db"
   :user        "root"
   :password    "13811381"})

(defn pool
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec))
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec)))]
    {:datasource cpds}))

(def pooled-db (delay (pool db-spec)))

(defn db-connection [] @pooled-db)

;; ------------------------------------------------------------ ;;

(defrecord Database [connection]
  component/Lifecycle
  (start [component]
    (println "starting Database component...")
    (let [pool (fn [spec]
                 (let [cpds (doto (ComboPooledDataSource.)
                              (.setDriverClass (:classname spec))
                              (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
                              (.setUser (:user spec))
                              (.setPassword (:password spec)))]
                   {:datasource cpds}))
          conn (pool db-spec)]
      (-> component
          (assoc :connection conn))))

  (stop [component]
    (println "stopping Database component...")
    (DataSources/destroy connection)
    (-> component)
    ))

;; (defn get-user [database username]
;;   (execute-query (:connection database)
;;                 "SELECT * FROM users"
;;                  username))

(defn new-database []
  (map->Database {}))

(comment

  (def x (db-connection))

  (jdbc/query (db-connection) "select * from User")

  ;;
  )
