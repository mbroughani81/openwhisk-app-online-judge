(ns io.github.mbroughani81.openwhisk-app
  (:require
   [io.github.mbroughani81.utils :as utils])
  (:import
   [com.google.gson JsonObject JsonPrimitive]))

;; (gen-class
;;   :name    io.github.mbroughani81.openwhisk_app.Main
;;   :methods [^:static [main [com.google.gson.JsonObject] com.google.gson.JsonObject]]
;;   :prefix  Main-)
(utils/OpenWhisk-Action-Entry "io.github.mbroughani81.openwhisk_app.Main")

(defn Main-main
  [^JsonObject args]
  (println "Hello, Worldddd")
  args)

(comment
  (def y ^JsonPrimitive (JsonPrimitive. "2"))
  (def x ^JsonObject (JsonObject.))
  (.add x "x" y)
  (class x)

  (x -main)

  (utils/print-ns)

  (print x)

  (-main [1 2 3])
  (class {})

  ;;
  )
