(ns io.github.mbroughani81.data.submit
  (:require [io.github.mbroughani81.data.problem :as data-problem]))

(defprotocol Submit-Access
  (<-problem [this])
  (<-code [this])
  (<-language [this])
  (<-status [this])
  (<-score [this]))

(defrecord Submit [problem code language status score]
  Submit-Access
  (<-problem [this] (-> problem))
  (<-code [this] (-> code))
  (<-language [this] (-> language))
  (<-status [this] (-> status))
  (<-score [this] (-> score)))

(defn make-submit [problem code language]
  {:pre [(instance? io.github.mbroughani81.data.problem.Problem problem)
         (some? code)
         (some? language)]}
  (map->Submit {:problem  problem
                :code     code
                :language language
                :status   "on_queue"
                :score    0}))

(comment

  ;;
  )
