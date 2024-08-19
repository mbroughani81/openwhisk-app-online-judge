(ns io.github.mbroughani81.data.problem
  (:require [clojure.spec.alpha :as spec]))

(defprotocol Problem-Access
  (<-problem-id [this])
  (<-tests [this]))

(defrecord Problem [problem-id tests]
  Problem-Access
  (<-problem-id [this] (-> problem-id))
  (<-tests [this] (-> tests)))

(spec/def ::in string?)
(spec/def ::out string?)
(spec/def ::test (spec/keys :req-un [::in ::out]))
(spec/def ::tests (spec/coll-of ::test))

(defn make-problem [problem-id tests]
  {:pre [(spec/valid? ::tests tests)]}
  (map->Problem {:problem-id problem-id
                 :tests      tests}))

(comment
  (spec/valid? ::tests [{:in "kk" :out "gg"}
                        {:in "kk1" :out "gg2"}])
  (make-problem [{:in "a" :out "b"} {:in "a" :out "o"}])

  ;;
  )
