(ns mustard.requires-test
  (:require [clojure.test :refer :all]
            [mustard.requires :as r]))

(def test-requires-code
  '((ns mustard.requires-test
      (:require [clojure.test :refer :all]
                [mustard.core :refer [foo bar]]
                [mustard.requires :as m]
                [mustard.tools
                 :refer [check] :rename {check check-tool} :as tools]
                [mustard.utils
                 :refer [scrape] :refer [map-html resettor]]
                [mustard.odd :as o :refer [fun] :as odd]))))

(deftest get-symbol-test
  (testing "Get symbol"
    (is (= (r/get-symbol nil :require) nil))
    (is (= (r/get-symbol '() :require) nil))
    (is (= (r/get-symbol '(ns mustard.test) 'ns) '(ns mustard.test)))
    (is (= (r/get-symbol '(:require [hello.world :as w]) :require)
           '(:require [hello.world :as w])))))

(deftest find-requires-test
  (testing "Finding requires"
    (is (= (r/find-requires test-requires-code)
           '[[clojure.test :refer :all]
             [mustard.core :refer [foo bar]]
             [mustard.requires :as m]
             [mustard.tools
              :refer [check] :rename {check check-tool} :as tools]
             [mustard.utils
              :refer [scrape] :refer [map-html resettor]]
             [mustard.odd :as o :refer [fun] :as odd]]))))

(deftest get-required-namespaces-test
  (testing "Getting required namespaces"
    (is (= (r/get-required-namespaces
             (r/find-requires test-requires-code))
           '#{m tools o odd}))))

(deftest get-required-functions-test
  (testing "Getting required functions"
    (is (= (r/get-required-functions
             (r/find-requires test-requires-code))
           '#{foo bar check-tool scrape map-html resettor fun}))))

(deftest rename-symbols-test
  (testing "Rename symbols"
    (is (= (r/rename-symbols '() {}) '()))
    (is (= (r/rename-symbols '(hello) '{world universe}) '(hello)))
    (is (= (r/rename-symbols '(hello world) '{world universe})
           '(hello universe)))
    (is (= (r/rename-symbols '(hello world) '{hello hi
                                              world universe})
           '(hi universe)))))

(deftest find-unused-requires-test
  (testing "Find unused requires"
    (is (= (r/find-unused-requires '()) {:functions #{} :namespaces #{}}))
    (is (= (r/find-unused-requires
             '((ns mustard.test
                 (:require [hello.world :as h]
                           [hi.universe :refer [run] :as u]
                           [hello.all :refer [something]
                            :rename {something so}]))))
           '{:functions #{run so}
             :namespaces #{h u}}))
    (is (= (r/find-unused-requires
             '((ns mustard.test
                 (:require [hello.world :as h]
                           [hi.universe :refer [run] :as u]))
                (defn hello []
                  (prn (run (h/prettify "Hello World!"))))))
           '{:functions #{}
             :namespaces #{u}}))))
