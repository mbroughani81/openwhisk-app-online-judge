(ns io.github.mbroughani81.eval-code
  (:require
   [com.stuartsierra.component :as component]
   [clj-http.client :as client]

   [io.github.mbroughani81.system :as system]
   [io.github.mbroughani81.utils :as utils]
   [io.github.mbroughani81.db-proto :as db-proto]
   [io.github.mbroughani81.data.user :as user]))

;; ------------------------------------------------------------ ;;

(defn run-once [code input]
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
                                       {:body         (-> input
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
    (-> result)))

;; should run the code for each input in input-list. will create an list of output
(defn main [{:keys [code input-list]
             :as   args}]
  {:pre []}
  (when (not (deref system/system-started?))
    (reset! system/system-started? true)
    (swap! system/system
           (fn [-system-]
             (component/start -system-))))
  (let [output-list (reduce (fn [acc input]
                              (let [output (run-once code input)]
                                (conj acc output)))
                            []
                            input-list)]
    (-> {:output-list output-list})))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.eval_code.Main")
(utils/OpenWhisk-Main main)

(comment
  (main {:code       "function main(params) { return { greeting: `Hello, ${params.name}!` }; }"
         :input-list [{:name "jafar sexi 1"}
                      {:name "jafar sexi 2"}
                      {:name "jafar sexi 3"}
                      ]})
  ;;
  )
