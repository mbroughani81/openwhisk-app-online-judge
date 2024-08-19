(ns io.github.mbroughani81.data.submit
  (:require [io.github.mbroughani81.data.problem :as data-problem]))

(defrecord Submit [problem code language status score])

(defn make-submit [problem code language]
  {:pre [(instance? io.github.mbroughani81.data.problem.Problem problem)]}
  (map->Submit {:problem  problem
                :code     code
                :language language
                :status   "on_queue"
                :score    0}))

(comment

  ;;
  )
