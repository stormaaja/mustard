(ns mustard.file)

(defn read-file-symbols [path]
  (read-string (str "(" (slurp path) ")")))
