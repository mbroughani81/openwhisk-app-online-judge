(ns io.github.mbroughani81.utils
  (:require [cheshire.core :as cheshire])
  (:import [com.google.gson JsonObject JsonPrimitive JsonParser]))

(defn json-obj->map [^JsonObject json-obj]
  (let [s (.toString json-obj)
        m (cheshire/parse-string s true)]
    (-> m)))

(defn ^JsonObject map->json-obj [m]
  (let [str      (cheshire/generate-string m)
        json-obj (.getAsJsonObject (JsonParser/parseString str))]
    (-> json-obj)))

;; (defmacro print-ns []
;;   `(println (str *ns*)))
;; (defmacro get-ns []
;;   `(-> (str *ns*)
;;        (clojure.string/replace #"-" "_")))

(defmacro OpenWhisk-Action-Entry [ns]
  `(gen-class
     :name    ~ns
     :methods [^:static ["main" [com.google.gson.JsonObject] com.google.gson.JsonObject]]
     :prefix  "Main-"))

(comment

  (do
    (def y ^JsonPrimitive (JsonPrimitive. 2))
    (def x ^JsonObject (JsonObject.))
    (.add x "x" y)
    (json-obj->map x)
    (json-obj->map "123")
    ;;
    ))
