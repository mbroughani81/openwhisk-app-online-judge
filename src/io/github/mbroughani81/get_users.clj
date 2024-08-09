(ns io.github.mbroughani81.get-users
  (:require
   [io.github.mbroughani81.utils :as utils])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.get_users.Main")

(defn Main-main
  [^JsonObject args]
  (println "Hello, Users Are Here!")
  args)

(comment
  ;;
  )
