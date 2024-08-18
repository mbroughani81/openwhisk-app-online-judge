(ns io.github.mbroughani81.data.submit
  (:require [io.github.mbroughani81.data.problem :as data-problem]))

(defrecord Submit [code language problem])

(defn make-submit [code language problem]
  {:pre [(instance? io.github.mbroughani81.data.problem.Problem problem)]}
  (map->Submit {:code code :language language :problem problem}))

(comment
  (let [problem (data-problem/make-problem [])]
    (= (type problem) Problem))


  (make-submit "code" "java" (data-problem/make-problem []))

  (make-submit "code" "java" ())

  ;;
  )
