(ns io.github.mbroughani81.create-problem
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]
   [io.github.mbroughani81.data.problem :as problem]))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [problem-id t-limit-sec m-limit-mb tests]}]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [-db-    (-> @system/system :db)
        problem (problem/make-problem problem-id
                                      t-limit-sec
                                      m-limit-mb
                                      tests)
        _       (println "problem => " problem)
        _       (db-proto/add-problem -db- problem)])
  (-> {:result "ok"}))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_problem.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:problem-id  "gcx31"
         :t-limit-sec 55
         :m-limit-mb  100
         :tests       [{:in "1 2" :out "2"}
                       {:in "2 2" :out "4"}
                       {:in "1 0" :out "0"}
                       {:in "1 1000" :out "1000"}
                       {:in "20 20" :out "40"}]})

  ;;
  )
