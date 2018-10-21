(ns mustard.file
  (:require [clojure.java.io :as io]
            [clojure.tools.reader :as r]
            [clojure.string :as cstr]))

(def chars {:par-open 40
            :par-close 41
            :end -1})

(defn skip-until-paren! [reader]
  (loop []
    (let [c (.read reader)]
      (cond
        (= c (:end chars)) nil
        (= c (:par-open chars)) (char c)
        :else (recur)))))

(defn read-next-block! [reader]
  (when-let [c (skip-until-paren! reader)]
    (let [sb (StringBuffer. (str c))]
      (loop [depth 1]
        (if (zero? depth)
          (str sb)
          (let [nc (.read reader)]
            (when (neg? nc) (Exception. "Unbalanced parenthesis"))
            (.append sb (char nc))
            (cond
              (= nc (:par-open chars)) (recur (inc depth))
              (= nc (:par-close chars)) (recur (dec depth))
              :else (recur depth))))))))

(defn remove-unevaluated-keywords [s]
  (cstr/replace s #"::" ":"))

(defn read-file-symbols [path]
  (with-open [reader (java.io.PushbackReader. (io/reader path))]
    (loop [coll '()]
      (if-let [b (read-next-block! reader)]
        (recur (conj coll (-> b remove-unevaluated-keywords r/read-string)))
        (reverse coll)))))

(defn clj-file? [file]
  (and (some? file) (some? (re-find #".*\.clj(s|c)?$" (.getName file)))))

(defn find-clj-files [folder]
  (filter
    clj-file?
    (file-seq (io/file folder))))
