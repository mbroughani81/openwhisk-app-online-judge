(ns io.github.mbroughani81.create-submit
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]
   [taoensso.timbre :as timbre]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.data.submit :as submit]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;



(defn main [{:keys [config
                    problem-id code language]
             :as   args}]
  (system/start-system config)
  (let [-db-           (-> @system/system :db)
        problem        (db-proto/get-problem -db- problem-id)
        _              (timbre/info "problem found => " problem)
        problem-found? (some? problem)
        submit         (when problem-found?
                         (submit/make-submit problem code language))
        db-result      (when problem-found?
                         (db-proto/add-submit -db- submit))
        submit-id      (-> db-result first :submit_id)
        ]
    (-> {:result    "ok"
         :submit-id submit-id})))

;; (utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_submit.Main")
;; (utils/OpenWhisk-Main main)
;; (utils/SAM-Action-Entry "io.github.mbroughani81.create_submit.Handler")
;; (utils/SAM-Main main)
(gen-class
  :name    "io.github.mbroughani81.create_submit.RequestHandler"
  :prefix  "RequestHandler-"
  :implements [com.amazonaws.services.lambda.runtime.RequestHandler]
  )
(defn RequestHandler-handleRequest
  [^java.util.Map args
   ^com.amazonaws.services.lambda.runtime.Context context]
  (let [
        ;; result# (into {} args#)
        ;; result# (~main result#)
        ;; result# (java.util.HashMap. {:message "gooz"})
        result (int 1)
        ]
    result))




(comment
  (main {:config     {:classname   "org.postgresql.Driver"
                      :subprotocol "postgresql"
                      :subname     "//172.17.0.1:5432/openwhisk_app_db"
                      :user        "postgres"
                      :password    "13811381"}
         :problem-id "gcx41"
         :code       "function main(params) { const numbers = params.input.split(' ').map(Number); const result = numbers[0] * numbers[1]; return { output: result.toString() }; }"
         :language   "js"})


  (macroexpand-1 '(utils/SAM-Action-Entry "io.github.mbroughani81.create_submit.Handler"))

  ;; clojure.spec.alpha
  ;;
  )
