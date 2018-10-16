(ns leiningen.mustard
  (:require [leiningen.core.eval :as eval]
            [leiningen.core.main :as main])
  (:import (clojure.lang ExceptionInfo)))

(defn get-mustard-dependency [project]
  (some
    #(when (= (first %) 'mustard) %)
    (:dependencies project)))

(defn has-mustard? [project]
  (some? (get-mustard-dependency project)))

(defn mustard [project & args]
  (let [project (if (has-mustard? project)
                  project
                  (update-in project [:dependencies]
                             conj ['mustard "RELEASE"]))
        opts project]
    (try
      (eval/eval-in-project
        project
        `(mustard.core/run-project '~opts ~@args)
        '(require 'mustard.core))
      (catch ExceptionInfo e
        (main/exit
          (:exit-code (ex-data e) 1))))))
