(defproject mustard "0.1.4"
  :description "Yet another Clojure static code analyze tool."
  :url "https://github.com/stormaaja/mustard"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.reader "1.3.0"]]
  :plugins [[lein-cljfmt "0.6.0"]
            [lein-kibit "0.1.6"]
            [lein-bikeshed "0.5.1"]
            [jonase/eastwood "0.3.1"]
            [lein-auto "0.1.3"]
            [lein-ancient "0.6.15"]
            [lein-cloverage "1.0.13"]]
  :cloverage {:html? false}
  :cljfmt {:indents {#".*" [[:block 0]]}}
  :aliases {"checkall" ["do"
                        ["kibit"]
                        ["bikeshed"]
                        ["eastwood"]
                        ["cljfmt" "check"]]}
  :profiles {:check {:plugins [[lein-mustard "0.1.4"]]}})
