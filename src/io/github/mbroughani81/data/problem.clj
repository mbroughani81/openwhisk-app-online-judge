(ns io.github.mbroughani81.data.problem
  (:require [clojure.spec.alpha :as spec]
            [io.github.mbroughani81.utils :as utils]))

(defprotocol Problem-Access
  (<-problem-id [this])
  (<-t-limit-sec [this])
  (<-m-limit-mb [this])
  (<-tests [this]))

(defrecord Problem [problem-id t-limit-sec m-limit-mb tests]
  Problem-Access
  (<-problem-id [this] (-> problem-id))
  (<-t-limit-sec [this] (-> t-limit-sec))
  (<-m-limit-mb [this] (-> m-limit-mb))
  (<-tests [this] (-> tests)))

(spec/def ::in string?)
(spec/def ::out string?)
(spec/def ::test (spec/keys :req-un [::in ::out]))
(spec/def ::tests (spec/coll-of ::test))

(defn make-problem [problem-id t-limit-sec m-limit-mb tests]
  {:pre [(utils/validate ::tests tests)]}
  (map->Problem {:problem-id  problem-id
                 :t-limit-sec t-limit-sec
                 :m-limit-mb  m-limit-mb
                 :tests       tests}))

(comment
  (spec/valid? ::tests [{:in "kk" :out "gg"}
                        {:in "kk1" :out "gg2"}])
  (make-problem [{:in "a" :out "b"} {:in "a" :out "o"}])

  ;;
  )
