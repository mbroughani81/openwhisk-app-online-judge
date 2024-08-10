(ns io.github.mbroughani81.get-users
  (:require
   [clojure.java.jdbc :as jdbc]
   [com.stuartsierra.component :as component]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db :as db])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))

;; ------------------------------------------------------------ ;;

(def cnt (atom 0))

;; ------------------------------------------------------------ ;;

(defn init-system []
  (let [config {}]
    (atom
      (component/system-map
        :db (db/new-database)))))
(def system (init-system))
(def system-started? (atom false))

;; ------------------------------------------------------------ ;;

(defn main [args]
  (swap! cnt inc)
  (let [_     (when (not @system-started?)
                (swap! system-started? (fn [x] true))
                (swap! system
                       (fn [-system-]
                         (component/start -system-))))
        -db-  (-> @system :db)
        users (jdbc/query (-> -db- :connection)
                          "SELECT * FROM User")]
    (-> {:users users
         :x     "this is x"
         :y     "this is y"
         :cnt   (deref cnt)})))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {})

  (-> @system :db)



  (keys x)

  (jdbc/query (-> db :connection) "select * from User")

  ;;
  )
