(ns io.github.mbroughani81.db
  (:require [com.stuartsierra.component :as component]
            [clojure.java.jdbc :as jdbc]
            [buddy.hashers :as hashers]

            [io.github.mbroughani81.db-proto :as db-proto])
  (:import (com.mchange.v2.c3p0 ComboPooledDataSource DataSources)))

;; ------------------------------------------------------------ ;;

(def db-spec
  {:classname   "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname     "//10.10.0.1:5432/openwhisk_app_db"
   :user        "postgres"
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
  ;; ------------------------------------------------------------ ;;
  db-proto/User-Repo
  ;; ------------------------------------------------------------ ;;
  (get-users [this]
    (jdbc/query connection
                "SELECT * FROM appusers"))
  (get-user [this username]
    (jdbc/query connection
                ["SELECT * FROM appusers WHERE username = ?" username]))
  (add-user [this user password]
    (let [hashed-password (hashers/derive password)
          _               (println "UUU => " user)]
      (jdbc/insert! connection
                    :appusers
                    {:username (-> user :username)
                     :password hashed-password})))
  ;; ------------------------------------------------------------ ;;
  component/Lifecycle
  ;; ------------------------------------------------------------ ;;
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
    (DataSources/destroy (-> connection :datasource))
    (-> component)))

(defn new-database []
  (map->Database {}))

(comment
  (def x (db-connection))

  (jdbc/query (db-connection) "select * from User")

  ;;
  )
