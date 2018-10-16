(ns mustard.symbols)

(defn get-symbols [code]
  (reduce
    (fn [c n]
      (if (coll? n)
        (into c (get-symbols n))
        (conj c n)))
    '()
    code))

(defn get-namespace-symbol [s]
  (when (and (symbol? s) (namespace s))
    (symbol (namespace s))))

(defn get-used-namespaces [symbols]
  (->> (map get-namespace-symbol symbols)
       (remove nil?)
       set))
