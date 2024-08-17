(ns io.github.mbroughani81.system
  (:require
   [com.stuartsierra.component :as component]
   [io.github.mbroughani81.db :as db]))

(defn init-system []
  (let [config {}]
    (component/system-map
      :db (db/new-database))))

(def system (atom (init-system)))
(def system-started? (atom false))

(defn reset-system []
  (when @system-started?
    (component/stop-system @system)
    (reset! system-started? false))
  (reset! system (init-system)))
