(ns simple-app.specs)
(s/explain :simple-app/last-name 1)
(s/explain :simple-app/last-name "foo")
(s/explain :simple-app/date-of-birth "1/1/1")
(s/explain :simple-app/date-of-birth (java.util.Date.))
(s/explain :simple-app/record {})
(s/explain :simple-app/record {:first-name "foo"
                               :last-name "bar"
                               :gender "neutral"
                               :favorite-color "chartreuse"
                               :date-of-birth (java.util.Date.)})

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns simple-app.record-service)
(require '[io.pedestal.http :as http])
(defonce server (atom nil))

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                       {::http/routes service/routes
                        ::http/type   :jetty
                        ::http/port   8890
                        ::http/join? false}))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))

(defn test-request [& params]
  (apply (partial test/response-for (::http/service-fn @server)) params))

(start-dev)

(ns simple-app.record-service)
(-> (test-request :post "/records"
                  :headers {"Content-Type" "text/plain"}
                  :body "boo | far | female | chartreuse | 1/1/2020")
    :body
    json/read-str
    json/pprint)

(ns simple-app.record-service)
(-> (test-request :get "/records/name")
    :body
    json/read-str
    json/pprint)

(ns simple-app.record-service)
(require '[simple-app.core :as core])
(require '[simple-app.record :as record])
(reset! database [])
(core/process-file-by-lines "resources/sample-file-2" record/parse (partial swap! database conj))

(ns simple-app.record-service)
(-> (test-request :get "/records/gender")
    :body
    json/read-str
    json/pprint)

(ns simple-app.record-service)
(-> (test-request :get "/records/birthdate")
    :body
    json/read-str
    json/pprint)

(ns simple-app.record-service)
(println (test-request :get "/records"))
(println (test-request :get "/records/"))
(println (test-request :get "/records/other"))

(ns simple-app.record-service)
(test-request :post "/records"
              :headers {"Content-Type" "text/plain"}
              :body "")

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns clojure.test)
(run-tests 'simple-app.record-test)

(ns simple-app.record-service)
(json/pprint (test-request :post "/records"
                           :headers {"Content-Type" "text/plain"}
                           :body ""))

(ns simple-app.system)
(stop)
(start)
