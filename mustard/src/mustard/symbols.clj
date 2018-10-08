(ns mustard.symbols)

(defn get-symbols [code]
  (reduce
    (fn [c n]
      (if (coll? n)
        (into c (get-symbols n))
        (conj c n)))
    '()
    code))
