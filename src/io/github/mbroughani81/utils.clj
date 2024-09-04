(ns io.github.mbroughani81.utils
  (:require [cheshire.core :as cheshire]
            [clojure.spec.alpha :as spec])
  (:import [com.google.gson JsonObject JsonPrimitive JsonParser]))

(defn json-obj->map [^JsonObject json-obj]
  (let [s (.toString json-obj)
        m (cheshire/parse-string s true)]
    (-> m)))

(defn ^JsonObject map->json-obj [m]
  (let [str      (cheshire/generate-string m)
        json-obj (.getAsJsonObject (JsonParser/parseString str))]
    (-> json-obj)))

(defmacro OpenWhisk-Action-Entry [ns]
  `(gen-class
     :name    ~ns
     :methods [^:static ["main" [com.google.gson.JsonObject] com.google.gson.JsonObject]]
     :prefix  "Main-"))

(defmacro OpenWhisk-Main [main]
  `(defn ~'Main-main
     [^JsonObject args#]
     (let [result# (io.github.mbroughani81.utils/json-obj->map args#)
           result# (~main result#)
           result# (io.github.mbroughani81.utils/map->json-obj result#)]
       (println "Hello, Users Are Here!")
       result#)))

(defn validate [spec x]
  (if (spec/valid? spec x)
    (-> true)
    (do
      (spec/explain spec x)
      (-> false))))

(comment

  ;;
  )
