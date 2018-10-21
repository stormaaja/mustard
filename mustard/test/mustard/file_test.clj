(ns mustard.file-test
  (:require [clojure.test :refer :all]
            [mustard.file :as f]
            [clojure.java.io :as io]))

(defn get-reader [s]
  (-> s
      (.getBytes)
      (java.io.ByteArrayInputStream.)
      (clojure.java.io/reader)))

(deftest skip-until-paren-test
  (testing "Skip until next parenthesis"
    (is (= (f/skip-until-paren! (get-reader " "))
          nil))
    (is (= (f/skip-until-paren! (get-reader "    (1 2 3)"))
          \())))

(deftest read-next-block-test
  (testing "Read next block"
    (is (= (f/read-next-block! (get-reader "(1 2 3) (2 3 4)"))
          "(1 2 3)"))
    (let [r (get-reader "(1 2 3 4) (2 3 4 5)")]
      (f/read-next-block! r)
      (is (= (f/read-next-block! r) "(2 3 4 5)")))
    (is (thrown-with-msg?
          Exception #"Unbalanced parenthesis"
          (f/read-next-block! (get-reader "((1 2 3)"))))))

(deftest remove-unevaluated-keywords-test
  (testing "Remove unevaluated keywords"
    (is (= (f/remove-unevaluated-keywords "{::some/ue-key \"value\"}")
           "{:some/ue-key \"value\"}"))
    (is (= (f/remove-unevaluated-keywords "{:valid-key \"value\"}")
           "{:valid-key \"value\"}"))))

(deftest clj-file-test
  (testing "Is file a Clojure file"
    (is (f/clj-file? (io/file "clojure.clj")))
    (is (f/clj-file? (io/file "clojure.cljs")))
    (is (f/clj-file? (io/file "clojure.cljc")))
    (is (not (f/clj-file? (io/file "clojure.cl"))))
    (is (not (f/clj-file? (io/file nil))))))
