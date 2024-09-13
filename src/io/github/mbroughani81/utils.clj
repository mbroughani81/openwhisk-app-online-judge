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

(defmacro SAM-Action-Entry [ns]
  `(gen-class
     :name    ~ns
     :implements [com.amazonaws.services.lambda.runtime.RequestHandler]
     :methods [["handleRequest" [java.util.Map
                                 com.amazonaws.services.lambda.runtime.Context]
                java.lang.Integer]]
     :prefix  "HandleRequest-"))

(defmacro OpenWhisk-Main [main]
  `(defn ~'Main-main
     [^JsonObject args#]
     (let [result# (io.github.mbroughani81.utils/json-obj->map args#)
           result# (~main result#)
           result# (io.github.mbroughani81.utils/map->json-obj result#)]
       result#)))

(defmacro SAM-Main [main]
  `(defn ~'HandleRequest-handleRequest
     [^java.util.Map args#
      ^com.amazonaws.services.lambda.runtime.Context context#]
     (let [
           ;; result# (into {} args#)
           ;; result# (~main result#)
           ;; result# (java.util.HashMap. {:message "gooz"})
           result# (int 1)
           ]
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
