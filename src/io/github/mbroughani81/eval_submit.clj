(ns io.github.mbroughani81.eval-submit
  (:require
   [com.stuartsierra.component :as component]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.submit :as submit]
   [io.github.mbroughani81.eval-code :as eval-code]))

(defn main [{:keys [submit-id]
             :as   args}]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [-db-        (-> @system/system :db)
        submit      (db-proto/get-submit -db- submit-id)
        _           (println "submitttt => " submit)
        input-list  (->> submit
                         submit/<-problem
                         :tests
                         (map :in)
                         (map #(-> {:input %}))
                         (into []))
        code        (submit/<-code submit)
        _           (clojure.pprint/pprint input-list)
        output-list (eval-code/main {:code       code
                                     :input-list input-list})
        output-list (-> output-list
                        (update :output-list
                                #(into []
                                       (map :output %))))
        _           (clojure.pprint/pprint output-list)
        ]))

(comment
  (system/reset-system)

  (main {:submit-id 35})

  ;;
  )
