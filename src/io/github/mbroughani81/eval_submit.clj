(ns io.github.mbroughani81.eval-submit
  (:require
   [com.stuartsierra.component :as component]

   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.submit :as submit]
   [io.github.mbroughani81.eval-code :as eval-code]))

(defn cal-score [result-list test-list]
  (let [problem-cnt   (count test-list)
        pairs         (partition 2 (interleave result-list test-list))
        _             (println "pairs => " pairs)
        correct-count (reduce (fn [acc [x y]]
                                (if (= x y)
                                  (inc acc)
                                  (-> acc)))
                              0
                              pairs)]
    (-> (/ correct-count problem-cnt)
        double)))

(defn main [{:keys [config
                    submit-id]
             :as   args}]
  (system/start-system config)
  (let [-db-              (-> @system/system :db)
        submit            (db-proto/get-submit -db- submit-id)
        _                 (println "submitttt => " submit)
        test-input-list   (->> submit
                               submit/<-problem
                               :tests
                               (map :in)
                               (into []))
        test-output-list  (->> submit
                               submit/<-problem
                               :tests
                               (map :out)
                               (into []))
        code              (submit/<-code submit)
        test-input-list-1 (->> test-input-list
                               (map #(-> {:input %}))
                               (into []))
        output-list       (eval-code/main {:code       code
                                           :input-list test-input-list-1})
        output-list       (-> output-list
                              (update :output-list
                                      #(into []
                                             (map :output %)))
                              :output-list)
        score             (cal-score test-output-list output-list)
        score             (* score 100)
        _                 (println "score => " score)
        ;; updating submit in db
        _                 (db-proto/update-submit -db-
                                                  submit-id
                                                  "done"
                                                  score)
        _                 (clojure.pprint/pprint ["test-input-list" test-input-list])
        _                 (clojure.pprint/pprint ["test-output-list" test-output-list])
        _                 (clojure.pprint/pprint ["output-list" output-list])
        ]
    (-> {:result "ok"})))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.eval_submit.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:config    {:classname   "org.postgresql.Driver"
                     :subprotocol "postgresql"
                     :subname     "//10.10.0.1:5432/openwhisk_app_db"
                     :user        "postgres"
                     :password    "13811381"}
         :submit-id 44})

  ;;
  )
