(ns io.github.mbroughani81.get-users
  (:require
   [io.github.mbroughani81.utils :as utils])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))


(defn main [args]
  (-> args))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main")
;; (clojure.pprint/pprint (macroexpand-1
;;                          '(utils/OpenWhisk-Main (fn [x] (-> x)))))
;; (utils/OpenWhisk-Main (fn [x] (-> x)))


(comment
  (macroexpand-1
    (utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main"))
  (clojure.pprint/pprint
    (macroexpand-1 '(utils/OpenWhisk-Main
                      main)))
  ;;
  )
