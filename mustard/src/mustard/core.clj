(ns mustard.core
  (:require [mustard.requires :as r]
            [mustard.file :as file]
            [clojure.string :as cstr]))

(defn find-unused-requires-in-file [f]
  {:path (.getPath f)
   :unused-requires (r/find-unused-requires (file/read-file-symbols f))})

(defn ok? [unused-requires]
  (and (empty? (:functions unused-requires))
       (empty? (:namespaces unused-requires))))

(def colors
  {:red "\u001B[31m"
   :green "\u001B[32m"})

(defn colored [s color]
  (str (get colors color) s "\u001B[0m"))

(defn remove-nils [coll]
  (filter some? coll))

(defn create-ok-message [result]
  (list (format "%s %s" (:path result) (colored "OK" :green))))

(defn create-error-message [result]
  (remove-nils
    (list (format "%s %s" (:path result) (colored "Fail" :red))
          (when (seq (get-in result [:unused-requires :functions]))
            (str "  Unused functions: "
                 (cstr/join
                   ", " (get-in result [:unused-requires :functions]))))
          (when (seq (get-in result [:unused-requires :namespaces]))
            (str "  Unused namespaces: "
                 (cstr/join
                   ", " (get-in result [:unused-requires :namespaces])))))))

(defn get-file-message [result]
  (if (ok? (:unused-requires result))
    (create-ok-message result)
    (create-error-message result)))

(defn find-clojure-files [paths]
  (reduce
    (fn [c n]
      (into
        c
        (file/find-clj-files n)))
    '()
    paths))

(defn enrich [report]
  (map
    (fn [r]
      (assoc r
             :success (ok? (:unused-requires r))
             :message (get-file-message r)))
    report))

(defn analyze-project [paths]
  (let [files (find-clojure-files paths)
        report (map find-unused-requires-in-file files)
        enriched-report (enrich report)]
    enriched-report))

(defn detect-exit-code [analysis]
  (if (some?
        (some
          #(when-not (:success %) %)
          analysis))
    1
    0))

(defn run-project [project & args]
  (println)
  (println "Checking source files with Mustard")
  (let [analysis (analyze-project (concat (:source-paths project)
                                          (:test-paths project)))
        exit-code (detect-exit-code analysis)
        bad-file-count (count (remove :success analysis))]
    (doseq [file-report analysis]
      (run! println (:message file-report)))
    (println (format "Files checked: %d" (count analysis)))
    (when (pos? bad-file-count)
      (println (format "Bad files found: %d" bad-file-count)))
    (println)
    (System/exit exit-code)))
