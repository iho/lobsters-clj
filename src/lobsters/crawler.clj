(ns lobsters.crawler
  (:require
   [clj-http.client :as client]
   [clojure.core.async
    :as a
    :refer [>! <! >!! <!! go chan buffer close! thread
            alts! alts!! timeout]]))
(use 'hickory.core)

(require '[hickory.select :as s])
(require '[hickory.render :as r])
(def base-url "https://lobste.rs")
(def body (:body (client/get "https://lobste.rs")))
(def parsed (-> body parse as-hickory))
(defn parse-main-page [parsed]
  (as-> (s/select  (s/descendant (s/class :comments_label) (s/tag :a))  parsed) v (map #(str  base-url (:href  (:attrs %))) v)))
(println (parse-main-page parsed))
(defn get-next-page [parsed] (str base-url (-> (s/select  (s/descendant (s/class :morelink) (s/tag :a))  parsed) first :attrs :href)))
(println (get-next-page parsed))



(def post-url "https://lobste.rs/s/xdcrsa/kubernetes_is_container_orchestration")
(def body (:body (client/get post-url)))
(def parsed (-> body parse as-hickory))


(-> (s/select (s/descendant (s/class :link) (s/tag :a)) parsed) first :attrs :href) ; link
(-> (s/select (s/descendant (s/class :story_liner) (s/class :score)) parsed) first :content first) ;  rating
(println)
(def comments (s/select  (s/descendant (s/class :comments_subtree) (s/class :comment) (s/class :details))  parsed))
(def root-comments (s/select (s/child (s/class :comments1) (s/class :comments_subtree))  parsed))
(println (count comments))
(println (count root-comments))
(-> (s/select  (s/child (s/class :comment_text))  (first comments)) first r/hickory-to-html) ; text


(loop [parent-comment (first root-comments)]
  (let [comment (s/select (s/child (s/class :comment)) parent-comment)]
    (println (:id comment))
    (if (= nil comment)
      (recur comment)
      () )))



(s/select (s/class :comment) (first root-comments))

