(ns mustard.symbols-test
  (:require [clojure.test :refer :all]
            [mustard.symbols :as s]))

(deftest get-symbols
  (testing "Get symbols"
    (is (= (set (s/get-symbols
                  '((ns mustard.test
                      (:require [hello.world :as h]))
                    (defn hello [w]
                      (prn (h/p w))))))
           (set '(ns mustard.test :require hello.world :as
                     h defn hello w prn h/p w))))))
