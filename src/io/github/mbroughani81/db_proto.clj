(ns io.github.mbroughani81.db-proto)

(defprotocol User-Repo
  (get-users [this])
  (add-user [this user password]))
