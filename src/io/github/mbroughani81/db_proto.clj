(ns io.github.mbroughani81.db-proto)

(defprotocol User-Repo
  (get-users [this])
  (get-user [this username])
  (add-user [this user password])
  (add-problem [this problem])
  (get-problem [this problem-id])
  (add-submit [this submit])
  (get-submit [this submit-id])
  (update-submit [this submit-id status score]))
