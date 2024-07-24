(ns io.github.mbroughani81.openwhisk-app
  (:import [com.google.gson JsonObject JsonPrimitive]))
(gen-class
  :name    io.github.mbroughani81.openwhisk_app.Main
  :methods [^:static [main [com.google.gson.JsonObject] com.google.gson.JsonObject]]
  :prefix  Main-)

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

  (print x)

  (-main [1 2 3])
  (class {})

  ;;
  )
