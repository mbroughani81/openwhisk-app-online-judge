(ns io.github.mbroughani81.get-users
  (:require
   [io.github.mbroughani81.utils :as utils])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))


(defn main [args]
  (-> {:x "this is x"
       :y "this is y"}))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main")
(utils/OpenWhisk-Main main)

(comment
  ;;
  )
