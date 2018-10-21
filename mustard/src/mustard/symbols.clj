(ns mustard.symbols)

(defn- evaluator [x] x)

(defn valid-code? [v]
  (some?
    (try
      (evaluator v)
      (catch Exception e
        nil))))

(defn get-symbols [code]
  (reduce
    (fn [c n]
      (cond
        (not (valid-code? n)) c
        (map? n) (into c (get-symbols (vals n)))
        (coll? n) (into c (get-symbols n))
        (symbol? n) (conj c n)
        :else c))
    '()
    code))

(defn get-namespace-symbol [s]
  (when (and (valid-code? s) (symbol? s) (namespace s))
    (symbol (namespace s))))

(defn get-used-namespaces [symbols]
  (->> (map get-namespace-symbol symbols)
       (remove nil?)
       set))
