(ns lobsters.core)


(require '[clojure.java.jdbc :as sql])


(def user (kc/select users))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
