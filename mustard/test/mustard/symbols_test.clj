(ns mustard.symbols-test
  (:require [clojure.test :refer :all]
            [mustard.symbols :as s]))

(deftest get-symbols-test
  (testing "Get symbols"
    (is (= (set (s/get-symbols
                  '((ns mustard.test
                      (:require [hello.world :as h]))
                     (def value1 #{:hello :other "world"})
                     (def value2 {:some-key "And Some value" :other-key 20})
                     (def value3 {:some-strange/thing "Value"})
                     (defn hello [w]
                       (prn (h/p w))))))
           (set '(ns mustard.test hello.world h
                     def value1 value2 value3
                     defn hello w prn h/p w))))))

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
