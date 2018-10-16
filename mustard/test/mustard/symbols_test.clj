(ns mustard.symbols-test
  (:require [clojure.test :refer :all]
            [mustard.symbols :as s]))

(deftest get-symbols-test
  (testing "Get symbols"
    (is (= (set (s/get-symbols
                  '((ns mustard.test
                      (:require [hello.world :as h]))
                     (defn hello [w]
                       (prn (h/p w))))))
           (set '(ns mustard.test :require hello.world :as
                     h defn hello w prn h/p w))))))

(deftest get-used-namespaces-test
  (testing "Get used namespaces"
    (is (= (s/get-used-namespaces '()) #{}))
    (is (= (s/get-used-namespaces '(defn hello h/p w prn ot/calculate))
           '#{h ot}))))

(deftest get-namespace-symbol-test
  (testing "Get namespace symbol"
    (is (= (s/get-namespace-symbol nil) nil))
    (is (= (s/get-namespace-symbol 'hello) nil))
    (is (= (s/get-namespace-symbol 'hello/world) 'hello))))
