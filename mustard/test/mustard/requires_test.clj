(ns mustard.requires-test
  (:require [clojure.test :refer :all]
            [mustard.requires :as r]))

(def test-requires-code
  '(ns mustard.requires-test
     (:require [clojure.test :refer :all]
               [mustard.core :refer [foo bar]]
               [mustard.requires :as m]
               [mustard.tools
                :refer [check] :rename {check check-tool} :as tools]
               [mustard.utils
                :refer [scrape] :refer [map-html resettor]]
               [mustard.odd :as o :refer [fun] :as odd])))

(deftest find-requires-test
  (testing "Finding requires"
    (is (= (r/find-requires test-requires-code)
           '([clojure.test :refer :all]
             [mustard.core :refer [foo bar]]
             [mustard.requires :as m]
             [mustard.tools
              :refer [check] :rename {check check-tool} :as tools]
             [mustard.utils
              :refer [scrape] :refer [map-html resettor]]
             [mustard.odd :as o :refer [fun] :as odd])))))

(deftest get-required-namespaces
  (testing "Getting required namespaces"
    (is (= (r/get-required-namespaces
             (r/find-requires test-requires-code))
           '#{m tools o odd}))))

(deftest get-required-functions
  (testing "Getting required functions"
    (is (= (r/get-required-functions
             (r/find-requires test-requires-code))
           '#{foo bar check-tool scrape map-html resettor fun}))))

(deftest test-rename-symbols
  (testing "Rename symbols"
    (is (= (r/rename-symbols '() {}) '()))
    (is (= (r/rename-symbols '(hello) '{world universe}) '(hello)))
    (is (= (r/rename-symbols '(hello world) '{world universe})
           '(hello universe)))
    (is (= (r/rename-symbols '(hello world) '{hello hi
                                              world universe})
           '(hi universe)))))
