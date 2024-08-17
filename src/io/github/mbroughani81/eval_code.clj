(ns io.github.mbroughani81.eval-code
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;

(def cnt (atom 0))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [code]
             :as   args}]
  (swap! cnt inc)
  (println "GOOZ")
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [url   "http://10.10.0.1:3233/api/v1/namespaces/guest/actions/rest-action?overwrite=true"
        -cnt- (deref cnt)]
    (client/put url
                {:body         (-> {:exec {:kind "nodejs:default"
                                           :code code}}
                                   (cheshire.core/generate-string))
                 :content-type :json
                 :basic-auth   ["23bc46b1-71f6-4ed5-8c54-816aa4f8c502"
                                "123zO3xZCLrMN6v2BKK1dXYFpXlPkccOFqm12CdAsMgRU4VrNZ9lyGVCGuMDGIwP"]}))
  (-> {:result "ok!"}))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.eval_code.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:code "function main(params) { return { message: 1 + 1 }; }"})

  (system/reset-system)

  (let [url  "http://localhost:3233/api/v1/namespaces/guest/actions/rest-action?overwrite=true"
        code "function main(params) { return { message: \"boob-e-sara\" }; }"]
    (client/put url
                {:body         (-> {:exec {:kind "nodejs:default"
                                           :code code}}
                                   (cheshire.core/generate-string))
                 :content-type :json
                 :basic-auth   ["23bc46b1-71f6-4ed5-8c54-816aa4f8c502" "123zO3xZCLrMN6v2BKK1dXYFpXlPkccOFqm12CdAsMgRU4VrNZ9lyGVCGuMDGIwP"]}))

  ;;
  )
