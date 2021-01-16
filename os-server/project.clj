(defproject os-server "0.1.0-SNAPSHOT"
  :description "Server application for controlling 40k objective marker lights"
  :url "https://github.com/Kimbsy/objective-secured"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[http-kit "2.4.0-alpha6"]
                 [javax.servlet/servlet-api "2.5"]
                 [metosin/reitit "0.3.9"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ring-cors "0.1.13"]
                 [ring/ring-jetty-adapter "1.7.1"]]
  :main ^:skip-aot objective-secured.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
