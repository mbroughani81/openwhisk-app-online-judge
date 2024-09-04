(ns io.github.mbroughani81.db
  (:require [com.stuartsierra.component :as component]
            [clojure.java.jdbc :as jdbc]
            [buddy.hashers :as hashers]

            [io.github.mbroughani81.db-proto :as db-proto]
            [io.github.mbroughani81.data.problem :as problem]
            [io.github.mbroughani81.data.submit :as submit])
  (:import (com.mchange.v2.c3p0 ComboPooledDataSource DataSources)
           (org.postgresql.util PGobject)
           ))

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

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value]
    (doto (PGobject.)
      (.setType "json")
      (.setValue (cheshire.core/generate-string value)))))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pgobj metadata idx]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "jsonb" (cheshire.core/parse-string value keyword)
        :else   value))))

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
  (add-problem [this problem]
    (let [problem-id  (-> problem problem/<-problem-id)
          t-limit-sec (-> problem problem/<-t-limit-sec)
          m-limit-mb  (-> problem problem/<-m-limit-mb)
          tests       (-> problem problem/<-tests)]
      (jdbc/insert! connection
                    :problem
                    {:problem_id  problem-id
                     :t_limit_sec t-limit-sec
                     :m_limit_mb  m-limit-mb
                     :tests       (jdbc/sql-value {:tests tests})})))
  (get-problem [this problem-id]
    (let [problems (jdbc/query connection
                               ["SELECT * FROM problem where problem_id = ?" problem-id])
          problem  (first problems)
          problem  (if (nil? problem)
                     (-> problem)
                     (problem/make-problem (-> problem :problem_id)
                                           (-> problem :t_limit_sec)
                                           (-> problem :m_limit_mb)
                                           (-> problem :tests :tests)))]
      (-> problem)))
  (add-submit [this submit]
    (let [problem-id (-> submit
                         submit/<-problem
                         problem/<-problem-id)
          code       (-> submit
                         submit/<-code)
          lang       (-> submit
                         submit/<-language)
          status     (-> submit
                         submit/<-status)
          score      (-> submit
                         submit/<-score)
          db-result  (jdbc/insert! connection
                                   :submit
                                   {:problem_id problem-id
                                    :code       code
                                    :lang       lang
                                    :status     status
                                    :score      score})]
      (-> db-result)))
  (get-submit [this submit-id]
    (let [submits    (jdbc/query connection
                                 ["SELECT * FROM submit where submit_id = ?" submit-id])
          submit-raw (first submits)
          problem-id (-> submit-raw :problem_id)
          problem    (db-proto/get-problem this problem-id)
          submit     (submit/make-submit problem
                                         (-> submit-raw :code)
                                         (-> submit-raw :lang))]
      (-> submit)))
  (update-submit [this submit-id status score]
    (jdbc/update! connection
                  :submit
                  {:status status
                   :score  score}
                  ["submit_id = ?" submit-id]))
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
  (io.github.mbroughani81.system/reset-system)
  (io.github.mbroughani81.system/start-system)

  (let [-db-   (-> @io.github.mbroughani81.system/system :db)
        result (db-proto/get-problem -db- "prob1")]
    )

  ;;
  )
