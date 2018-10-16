(ns mustard.requires
  (:require [mustard.symbols :as s]
            [clojure.set :refer [difference]]))

(defn get-symbol [coll k]
  (when (and (seq? coll)
             (= (first coll) k))
    coll))

(defn find-requires [code]
  (->> (some #(get-symbol % 'ns) code)
       (rest)
       (some #(get-symbol % :require))
       (rest)))

(defn reduce-requires [f requires]
  (reduce
    (fn [c n]
      (loop [coll (rest n)
             nss c]
        (if (empty? coll)
          nss
          (let [[coll nss] (f coll nss)]
            (recur coll nss)))))
    #{}
    requires))

(defn get-required-namespaces [requires]
  (reduce-requires
    (fn [coll nss]
      (if (= (first coll) :as)
        [(drop 2 coll) (conj nss (last (take 2 coll)))]
        [(rest coll) nss]))
    requires))

(defn rename-symbols [symbols rename-map]
  (map
    #(get rename-map % %)
    symbols))

(defn conj-refers [fns options]
  (let [refers (second options)
        rename-map (if (and (= 4 (count options))
                            (= (nth options 2) :rename))
                     (nth options 3)
                     {})]
    (if (coll? refers)
      (apply conj fns (rename-symbols refers rename-map))
      fns)))

(defn get-required-functions [requires]
  (reduce-requires
    (fn [coll nss]
      (if (= (first coll) :refer)
        [(drop 2 coll) (conj-refers nss (take 4 coll))]
        [(rest coll) nss]))
    requires))

(defn summarize-requires [r]
  {:functions (get-required-functions r)
   :namespaces (get-required-namespaces r)})

(defn find-unused-requires [code]
  (let [requires (summarize-requires (find-requires code))
        symbols (set (s/get-symbols (rest code)))]
    {:functions (difference (:functions requires) symbols)
     :namespaces (difference
                   (:namespaces requires) (s/get-used-namespaces symbols))}))
