(ns io.github.mbroughani81.get-users
  (:require
   [io.github.mbroughani81.utils :as utils])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))

(def cnt (atom 0))

(defn main [args]
  (swap! cnt inc)
  (-> {:x   "this is x"
       :y   "this is y"
       :cnt (deref cnt)}))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main")
(utils/OpenWhisk-Main main)

(comment
  ;;
  @cnt
  cnt
  (swap! cnt inc)


  )
