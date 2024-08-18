(ns io.github.mbroughani81.eval-code
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;

(defn main [{:keys [code param]
             :as   args}]
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [create-action-url "http://10.10.0.1:3233/api/v1/namespaces/guest/actions/rest-action?overwrite=true"
        invoke-action-url "http://10.10.0.1:3233/api/v1/namespaces/guest/actions/rest-action?blocking=true"
        delete-action-url "http://10.10.0.1:3233/api/v1/namespaces/guest/actions/rest-action?blocking=true"
        auth              ["23bc46b1-71f6-4ed5-8c54-816aa4f8c502"
                           "123zO3xZCLrMN6v2BKK1dXYFpXlPkccOFqm12CdAsMgRU4VrNZ9lyGVCGuMDGIwP"]
        _                 (client/put create-action-url
                                      {:body         (-> {:exec {:kind "nodejs:default"
                                                                 :code code}}
                                                         (cheshire.core/generate-string))
                                       :content-type :json
                                       :basic-auth   auth})
        response          (client/post invoke-action-url
                                       {:body         (-> param
                                                          (cheshire.core/generate-string))
                                        :content-type :json
                                        :basic-auth   auth})
        result            (-> response
                              :body
                              (cheshire.core/parse-string keyword)
                              :response
                              :result)
        _                 (client/delete create-action-url
                                         {:body         (-> {:exec {:kind "nodejs:default"
                                                                    :code code}}
                                                            (cheshire.core/generate-string))
                                          :content-type :json
                                          :basic-auth   auth})]
    (-> {:result result})))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.eval_code.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:code  "function main(params) { return { greeting: `Hello, ${params.name}!` }; }"
         :param {:name "jafar sexi"}})

  (system/reset-system)

  (let [url-1    "http://localhost:3233/api/v1/namespaces/guest/actions/rest-action?overwrite=true"
        code     "function main(params) { return { greeting: `Hello, ${params.name}!` }; }"
        result-1 (client/put url-1
                             {:body         (-> {:exec {:kind "nodejs:default"
                                                        :code code}}
                                                (cheshire.core/generate-string))
                              :content-type :json
                              :basic-auth   ["23bc46b1-71f6-4ed5-8c54-816aa4f8c502" "123zO3xZCLrMN6v2BKK1dXYFpXlPkccOFqm12CdAsMgRU4VrNZ9lyGVCGuMDGIwP"]})
        body-1   (-> result-1 :body (cheshire.core/parse-string))
        url-2    "http://localhost:3233/api/v1/namespaces/guest/actions/rest-action?blocking=true"
        result-2 (client/post url-2
                              {:body         (-> {:name "jafar"}
                                                 (cheshire.core/generate-string))
                               :content-type :json
                               :basic-auth   ["23bc46b1-71f6-4ed5-8c54-816aa4f8c502" "123zO3xZCLrMN6v2BKK1dXYFpXlPkccOFqm12CdAsMgRU4VrNZ9lyGVCGuMDGIwP"]})
        body-2   (-> result-2 :body (cheshire.core/parse-string keyword) :response :result)]
    ;; (clojure.pprint/pprint body)
    (clojure.pprint/pprint body-1)
    (clojure.pprint/pprint body-2))

;;
  )
