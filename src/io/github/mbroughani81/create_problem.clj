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

(defn main [args]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [-db-       (-> @system/system :db)
        problem-id (-> args :problem-id)
        _          (println "problem-id => " problem-id)
        _          (println "tests =>" args)
        problem    (problem/make-problem (-> args :problem-id)
                                         (-> args :tests))
        _          (println "problem => " problem)
        _          (db-proto/add-problem -db- problem)])
  (println "args => " args)
  (-> {:result "ok"}))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_problem.Main")
(utils/OpenWhisk-Main main)

(comment

  (main {:problem-id "gcx3"
         :tests      [{:in "1 2" :out "3"}
                      {:in "2 2" :out "4"}
                      {:in "20 20" :out "40"}]})

  (system/init-system)

;;
  )
