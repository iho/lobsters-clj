(ns lobsters.crawler
  (:require
   [clj-http.client :as client]
   [clojure.core.async
    :as a
    :refer [>! <! >!! <!! go chan buffer close! thread
            alts! alts!! timeout]]))
(use 'hickory.core)
(require '[hickory.select :as s])
(def body (:body (client/get "https://lobste.rs")))
(def parsed (-> body parse as-hickory))
(as-> (s/select  (s/descendant (s/class :comments_label) (s/tag :a))  parsed) v (get-in [:attrs :href] v))
(println (-> (s/select  (s/descendant (s/class :morelink) (s/tag :a))  parsed) first :attrs :href))