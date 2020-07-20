(ns simple-app.record-service
  (:require [clojure.data.json :as json]
            [clojure.walk :as walk]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test]
            [simple-app.record :as record]))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok (partial response 200))
(def created (partial response 201))
(def bad-request (partial response 400))



(defonce database (atom []))

(defn record-insert
  [dbval new-record]
  (conj dbval new-record))

(def db-interceptor
  {:name :database-interceptor
   :enter
   (fn [context]
     (update context :request assoc :database @database))
   :leave
   (fn [context]
     (if-let [[op & args] (:tx-data context)]
       (do
         (apply swap! database op args)
         (assoc-in context [:request :database] @database))
       context))})




(defn parse-record [unparsed-record]
  (when (string? unparsed-record)
    (record/parse unparsed-record)))

(defn sorted-records
  [data sort-by-param]
  (case sort-by-param
    "name"
    (sort-by :last-name record/by-last-name data)
    "gender"
    (sort-by :gender record/by-gender data)
    "birthdate"
    (sort-by :date-of-birth record/by-birth-date data)
    nil))

(defn with-formated-dates
  "Given a data structure formats dates as `MM/dd/yyyy`"
  [data]
  (walk/postwalk (fn [x]
                   (if (= (class x)
                          java.util.Date)
                     (.format (java.text.SimpleDateFormat. "MM/dd/yyyy") x)
                     x))
                 data))

(defn record->json [data]
  (json/write-str (with-formated-dates data)))

(defn transform-to-json
  [response]
  (-> response
      (update :body record->json)
      (assoc-in [:headers "Content-Type"] "application/json")))



(def record-render
  {:name :record-render
   :leave
   (fn [context]
     (if-let [item (:result context)]
       (assoc context :response (ok item))
       context))})

(def record-create
  {:name :record-create
   :enter
   (fn [context]
     (let [body-stream (get-in context [:request :body])
           unparsed-record (slurp body-stream)
           new-record (parse-record unparsed-record)]
       (if new-record
         (-> context
             (assoc :tx-data [record-insert new-record])
             (assoc :response (created new-record)))
         (assoc context :response (bad-request "invalid record")))))})

(def list-view
  {:name :list-view
   :enter
   (fn [context]
     (if-let [sort-by-param (get-in context [:request :path-params :sort-by])]
       (if-let [records (sorted-records (get-in context [:request :database]) sort-by-param)]
         (assoc context :result records)
         context)
       context))})

(def coerce-body
  {:name ::coerce-body
   :leave
   (fn [context]
     (update-in context [:response] transform-to-json))})

(def routes
  (route/expand-routes
   #{["/records" :post [coerce-body db-interceptor record-create]]
     ["/records/:sort-by" :get [coerce-body record-render db-interceptor list-view]]}))
