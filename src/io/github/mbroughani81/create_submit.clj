(ns io.github.mbroughani81.create-submit
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [code param]
             :as   args}]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-)))))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_submit.Main")
(utils/OpenWhisk-Main main)

(comment

  ;;
  )
