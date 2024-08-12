(ns io.github.mbroughani81.system
  (:require
   [com.stuartsierra.component :as component]
   [io.github.mbroughani81.db :as db]))

(defn init-system []
  (let [config {}]
    (atom
      (component/system-map
        :db (db/new-database)))))
(def system (init-system))
(def system-started? (atom false))
