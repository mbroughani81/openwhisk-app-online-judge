(ns io.github.mbroughani81.get-users
  (:require
   [clojure.java.jdbc :as jdbc]
   [com.stuartsierra.component :as component]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db :as db]
   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.db-proto :as db-proto]))

;; ------------------------------------------------------------ ;;

(def cnt (atom 0))

;; ------------------------------------------------------------ ;;

(defn main [args]
  (swap! cnt inc)
  (let [_     (when (not (deref system/system-started?))
                (reset! system/system-started? true)
                (swap! system/system
                       (fn [-system-]
                         (component/start -system-))))
        -db-  (-> @system/system :db)
        users (db-proto/get-users -db-)]
    (-> {:users users
         :cnt   (deref cnt)})))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {})

  (keys x)
  (system/reset-system)

  (jdbc/query (-> db :connection) "select * from User")

  ;;
  )
