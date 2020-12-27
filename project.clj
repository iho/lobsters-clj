(defproject lobsters "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[me.flowthing/sigel "1.0.1"]
                 [org.clojure/core.async "1.3.610"]
                 [clj-http "3.10.3"]
                 [korma "0.4.3"]
                 [hickory "0.7.1"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [org.clojure/clojure "1.10.1"]]
  :repl-options {:init-ns lobsters.core})
