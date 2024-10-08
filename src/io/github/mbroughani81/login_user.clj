(ns io.github.mbroughani81.login-user
  (:require
   [com.stuartsierra.component :as component]
   [buddy.hashers :as hashers]

   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.db-proto :as db-proto]))

;; ------------------------------------------------------------ ;;

(def cnt (atom 0))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [username password]
             :as   args}]
  (swap! cnt inc)
  (let [_             (when (not (deref system/system-started?))
                        (reset! system/system-started? true)
                        (swap! system/system
                               (fn [-system-]
                                 (component/start -system-))))
        -db-          (-> @system/system :db)
        -cnt-         (-> cnt deref)
        fetched-users (-> (db-proto/get-user -db- username))]
    (if (= (count fetched-users) 0)
      (-> {:result "username not found"
           :cnt    -cnt-})
      (let [db-user     (first fetched-users)
            is-pass-ok? (-> (hashers/verify password (-> db-user :password))
                            :valid)]
        (if (not is-pass-ok?)
          (-> {:result "password is wrong!"
               :cnt    -cnt-})
          (-> {:result "login successful"
               :cnt    -cnt-}))))))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.login_user.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:username "m2"
         :password "13811381"})

  ;;
  )
