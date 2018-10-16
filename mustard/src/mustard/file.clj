(ns mustard.file
  (:require [clojure.java.io :as io]))

(defn read-file-symbols [path]
  (read-string (str "(" (slurp path) ")")))

(defn clj-file? [file]
  (some? (re-find #".*\.cljs?$" (.getName file))))

(defn find-clj-files [folder]
  (filter
    clj-file?
    (file-seq (io/file folder))))
