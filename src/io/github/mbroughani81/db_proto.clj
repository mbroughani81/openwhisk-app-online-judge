(ns io.github.mbroughani81.db-proto)

(defprotocol User-Repo
  (get-users [this])
  (get-user [this username])
  (add-user [this user password]))
