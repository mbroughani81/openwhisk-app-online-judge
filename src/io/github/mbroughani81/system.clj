(ns io.github.mbroughani81.system
  (:require
   [com.stuartsierra.component :as component]

   [io.github.mbroughani81.db :as db]))

(def system (atom nil))
(def system-started? (atom false))

(defn gen-system-map [config]
  (let [system     (component/system-map
                     :db (db/new-database))
        _          (println "raw-config => " config)
        db-config  {:db {:db-spec {:classname   (-> config :classname)
                                   :subprotocol (-> config :subprotocol)
                                   :subname     (-> config :subname)
                                   :user        (-> config :user)
                                   :password    (-> config :password)}}}
        new-system (merge-with merge
                               system
                               db-config)]
    (-> new-system)))

(defn init-system [system-map]
  (println "!!!RESTARTING SYSTEM!!!")
  (println "!!!SYSTEM STATUS : " @system-started? "!!!")
  (when @system-started?
    (component/stop-system @system)
    (reset! system-started? false))
  (reset! system system-map))

(defn start-system [config]
  (if (not @system-started?)
    (do
      (when (nil? @system)
        (let [system-map (gen-system-map config)]
          (init-system system-map)))
      (reset! system-started? true)
      (swap! system
             (fn [-system-]
               (component/start -system-))))))

(comment
  (keys @system)
  (init-system "resources/configs/create-problem-dev.edn")
  (def ss
    (init-system "resources/configs/create-problem-dev.edn"))

  (def p "resources/configs/create-problem-dev.edn")
  (def x (component/system-map
           :db (db/new-database)))
  (def y (aero/read-config p))
  x
  (keys x)
  (-> x :db)
  y
  (keys y)
  (merge-with merge y {:db {:connection nil :db-spec nil}})
  (merge-with merge y {:db nil})

  (merge {:db-spec {:a "123"}} {:connection nil :db-spec nil})
  (merge {:connection nil :db-spec nil} {:db-spec {:a "123"}})

;;
  )
