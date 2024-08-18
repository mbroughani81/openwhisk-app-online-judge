(ns io.github.mbroughani81.data.problem)

(defrecord Problem [tests])

;; TODO add pre checks
(defn make-problem [tests]
  (map->Problem {:tests tests}))
