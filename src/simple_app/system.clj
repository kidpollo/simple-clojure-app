(ns simple-app.system
  (:require [io.pedestal.http :as http]
            [simple-app.record-service :as service]
            [simple-app.record :as record]
            [simple-app.core :as core]))

(def system nil)

(def service-map
  {::http/routes service/routes
   ::http/type   :jetty
   ::http/port   8080
   ::http/join?  false})

(defn stop
  "Stops the app and clears the database"
  []
  (when system
    (http/stop (:app system)))
  (reset! service/database [])
  (alter-var-root #'system (constantly nil)))

(defn start
  "Resets the app database and starts the app with default database"
  []
  (core/process-file-by-lines "resources/sample-file-2" record/parse (partial swap! service/database conj))
  (alter-var-root #'system
                  merge
                  {:app (http/start (http/create-server service-map))}))

(defn -main []
  (start))
