(ns mustard.file
  (:require [clojure.java.io :as io]
            [clojure.tools.reader :as r]
            [clojure.string :as cstr]))

(defn remove-unevaluated-keywords [s]
  (cstr/replace s #"::" ":"))

(defn wrap-with-parenthesis [s]
  (format "(%s)" s))

(defn read-file-symbols [path]
  (try
    (->> (slurp path)
         remove-unevaluated-keywords
         wrap-with-parenthesis
         (r/read-string {:read-cond :allow}))
    (catch clojure.lang.ExceptionInfo e
      (throw (ex-info (.getMessage e) (assoc (ex-data e) :path path))))))

(defn clj-file? [file]
  (and (some? file) (some? (re-find #".*\.cljc?$" (.getName file)))))

(defn find-clj-files [folder]
  (filter
    clj-file?
    (file-seq (io/file folder))))
