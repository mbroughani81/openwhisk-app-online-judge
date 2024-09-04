(ns io.github.mbroughani81.eval-submit
  (:require
   [com.stuartsierra.component :as component]

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

(defn main [{:keys [submit-id]
             :as   args}]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
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
        ]))

(comment
  (system/reset-system)

  (main {:submit-id 36})

  ;;
  )
