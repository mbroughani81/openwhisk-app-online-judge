(ns io.github.mbroughani81.register-user
  (:require
   [com.stuartsierra.component :as component]
   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]

   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;

(def cnt (atom 0))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [username password]
             :as   args}]
  (swap! cnt inc)
  (-> cnt deref)
  (let [_     (when (not (deref system/system-started?))
                (reset! system/system-started? true)
                (swap! system/system
                       (fn [-system-]
                         (component/start -system-))))
        -db-  (-> @system/system :db)
        user  (user/make-User username)
        users (db-proto/add-user -db- user password)]
    (-> {:message "adding user is successful"
         :cnt     (deref cnt)})))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.register_user.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:username "m4"
         :password "13811381"})

  ;;
  )
