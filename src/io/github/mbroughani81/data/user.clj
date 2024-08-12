(ns io.github.mbroughani81.data.user)

(defrecord User [username])

(defn make-User [username]
  (map->User {:username username}))
