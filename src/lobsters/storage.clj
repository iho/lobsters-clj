(ns lobsters.storage) 
(:require '[clojure.java.jdbc :as sql])

(def postgres-uri "postgresql://test_user:qwerty@localhost:5432/test_database")
(def create-commands [(sql/create-table-ddl :users [[:id :serial :primary :key]
                                                    [:username :text]
                                                    [:datetime :timestamp]])

                      (sql/create-table-ddl :posts [[:id :serial :primary :key]
                                                    [:text :text]
                                                    [:rating :int]
                                                    [:user_id :int "references users (id)"]
                                                    [:number_of_comments :int]
                                                    [:datetime :timestamp]])

                      (sql/create-table-ddl :tags [[:id :int :primary :key]
                                                   [:name :text]
                                                   [:slug :text]
                                                   [:count :int]
                                                   [:datetime :timestamp]])

                      (sql/create-table-ddl :comments [[:id :serial :primary :key]
                                                       [:text :text]
                                                       [:rating :int]
                                                       [:parent_id :int "references comments (id)"]
                                                       [:user_id :int "references users (id)"]
                                                       [:post_id :int "references posts (id)"]
                                                       [:datetime :timestamp]])])
(defn create-database [] (sql/db-do-commands postgres-uri create-commands))
(defn drop-dabase [] (sql/db-do-commands postgres-uri [(sql/drop-table-ddl :comments)
                                                       (sql/drop-table-ddl :tags)
                                                       (sql/drop-table-ddl :posts)
                                                       (sql/drop-table-ddl :users)]))


; (sql/insert! postgres-uri :users {:username "Ihor"})
; (sql/query postgres-uri ["select * from users"])

(def database
  (ref
   {:users {}
    :comments {}
    :posts {}
    :tags  {}}))

(defn add-user [type user]
  (if (= type "memory")
    (dosync
     (alter database assoc-in [:users (:id user)] user))
    (sql/insert! postgres-uri  :users user)))

(defn add-post [type post]
  (if (= type "memory")
    (dosync
     (alter database assoc-in [:posts (:id post)] post))
    (sql/insert! postgres-uri  :posts post)))

(defn add-tag [type tag]
  (if (= type "memory")
    (dosync
     (alter database assoc-in [:tags (:id tag)] tag))
    (sql/insert! postgres-uri  :tag tag)))

(defn add-comment [type comment post-id]
  (if (= type "memory")
    (dosync
     (alter database assoc-in [:tags (:id comment) post-id] comment))
    ((sql/insert! postgres-uri  :comments comment))))

(defn get-stats [type]
  (if (= type "memory")
    (let [db database]
      {:users (count (:users db))
       :tags (count (:tags db))
       :posts (count (:posts db))
       :comments (count (vals (:comments db)))})
    {:users (sql/query postgres-uri ["select count(*) from users"])
     :tags (sql/query postgres-uri ["select count(*) from users"])
     :posts (sql/query postgres-uri ["select count(*) from users"])
     :comments (sql/query postgres-uri ["select count(*) from users"])}))
(println (get-stats "memor"))