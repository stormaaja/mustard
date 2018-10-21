(ns mustard.file-test
  (:require [clojure.test :refer :all]
            [mustard.file :as f]
            [clojure.java.io :as io]))

(deftest remove-unevaluated-keywords-test
  (testing "Remove unevaluated keywords"
    (is (= (f/remove-unevaluated-keywords "{::some/ue-key \"value\"}")
           "{:some/ue-key \"value\"}"))
    (is (= (f/remove-unevaluated-keywords "{:valid-key \"value\"}")
           "{:valid-key \"value\"}"))))

(deftest wrap-with-parenthesis-test
  (testing "Wrap with parenthesis"
    (is (= (f/wrap-with-parenthesis "hello")
           "(hello)"))
    (is (= (f/wrap-with-parenthesis nil)
           "(null)"))))

(deftest clj-file-test
  (testing "Is file a Clojure file"
    (is (f/clj-file? (io/file "clojure.clj")))
    (is (f/clj-file? (io/file "clojure.cljs")))
    (is (f/clj-file? (io/file "clojure.cljc")))
    (is (not (f/clj-file? (io/file "clojure.cl"))))
    (is (not (f/clj-file? (io/file nil))))))
