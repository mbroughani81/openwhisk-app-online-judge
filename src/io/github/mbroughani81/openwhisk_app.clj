(ns io.github.mbroughani81.openwhisk-app
  (:require
   [io.github.mbroughani81.utils :as utils])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))

(defn main [args]
  (-> args))

(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.openwhisk_app.Main")
(utils/OpenWhisk-Main main)

(comment
  ;;
  )
