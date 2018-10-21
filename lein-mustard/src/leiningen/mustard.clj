(ns leiningen.mustard
  (:require [leiningen.core.eval :as eval]
            [leiningen.core.main :as main]
            [mustard.core :as m])
  (:import (clojure.lang ExceptionInfo)))

(defn ^:no-project-needed mustard [project & args]
  (try
    (m/run-project project args)
    (catch ExceptionInfo e
      (main/exit
        (:exit-code (ex-data e) 1)))))
