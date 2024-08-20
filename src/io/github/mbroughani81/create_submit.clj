(ns io.github.mbroughani81.create-submit
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.data.submit :as submit]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [problem-id code language]
             :as   args}]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [-db-    (-> @system/system :db)
        problem (db-proto/get-problem -db- problem-id)
        submit  (submit/make-submit problem code language)
        _       (db-proto/add-submit -db- submit)]
    ;; creating submit in db
    ;; evaluating the code with test
    ;; scroting and updating the db
    (-> {:result "ok"})
    ))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_submit.Main")
(utils/OpenWhisk-Main main)

(comment
  (system/reset-system)

  (main {:problem-id "prob1"
         :code       "function main(params) {return {message: `HELLO, ${params.name}`};}"
         :language   "js"})

  ;;
  )
