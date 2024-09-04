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
  (let [-db-           (-> @system/system :db)
        ;; creating submit in db
        problem        (db-proto/get-problem -db- problem-id)
        _              (println "problem => " problem)
        problem-found? (some? problem)
        submit         (when problem-found?
                         (submit/make-submit problem code language))
        db-result      (when problem-found?
                         (db-proto/add-submit -db- submit))
        submit-id      (-> db-result first :submit_id)
        _              (println "gooz => " db-result submit-id)
        ;; evaluating the code with test
        ;; _              (when problem-found?
        ;;                  ())
        ]
    ;; scroting and updating the db
    (-> {:result "ok"})
    ))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.create_submit.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:problem-id "gcx31"
         :code       "function main(params) { const numbers = params.input.split(' ').map(Number); const result = numbers[0] * numbers[1]; return { output: result.toString() }; }"
         :language   "js"})
  ;;
  )
