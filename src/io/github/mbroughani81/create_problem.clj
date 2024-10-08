(ns io.github.mbroughani81.create-problem
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]
   [taoensso.timbre :as timbre]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]
   [io.github.mbroughani81.data.problem :as problem]))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [config
                    problem-id t-limit-sec m-limit-mb tests]}]
  (system/start-system config)
  (let [-db-    (-> @system/system :db)
        problem (problem/make-problem problem-id
                                      t-limit-sec
                                      m-limit-mb
                                      tests)
        _       (timbre/info "problem => " problem)
        _       (db-proto/add-problem -db- problem)])
  (-> {:result "ok"}))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_problem.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:problem-id  "gcx36"
         :t-limit-sec 55
         :m-limit-mb  100
         :tests       [{:in "1 2" :out "2"}
                       {:in "2 2" :out "4"}
                       {:in "1 0" :out "0"}
                       {:in "1 1000" :out "1000"}
                       {:in "20 20" :out "40"}]})
  (main {:config      {:classname   "org.postgresql.Driver"
                       :subprotocol "postgresql"
                       :subname     "//172.17.0.1:5432/openwhisk_app_db"
                       :user        "postgres"
                       :password    "13811381"}
         :problem-id  "prob1"
         :t-limit-sec 55
         :m-limit-mb  100
         :tests       [{:in "1 2" :out "2"}
                       {:in "2 2" :out "4"}
                       {:in "1 0" :out "0"}
                       {:in "1 1000" :out "1000"}
                       {:in "20 20" :out "400"}]})

  (system/init-system (system/gen-system-map "resources/configs/create-problem-dev.edn"))

  ;;
  )
