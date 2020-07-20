(ns simple-app.core
  (:require [simple-app.record :as record]
            [clojure.java.io :as io]))

(defn process-file-by-lines
  "Process file reading it line-by-line
  https://stackoverflow.com/questions/25948813/read-line-by-line-for-big-files"
  ([file]
   (process-file-by-lines file identity))
  ([file process-fn]
   (process-file-by-lines file process-fn println))
  ([file process-fn output-fn]
   (with-open [rdr (io/reader file)]
     (doseq [line (line-seq rdr)]
       (output-fn
         (process-fn line))))))

(defn -main [input-file]
  (process-file-by-lines input-file record/parse))

(ns simple-app.core)
(require '[clojure.pprint :as pprint])
(require '[clojure.spec.alpha :as s])
(require '[simple-app.spec])

(defn -main [input-file]
  (let [data (atom [])]
    (process-file-by-lines input-file record/parse (partial swap! data conj))
    (prn "Validating data")
    (s/explain :simple-app/records @data)
    (when (s/valid? :simple-app/records @data)
      (prn "Sorted by gender, then last-name")
      (pprint/print-table (->> @data
                               (sort-by :last-name)
                               (sort-by :gender record/by-gender)))
      (prn "Sorted by birth-date, ascending")
      (pprint/print-table (->> @data
                               (sort-by :date-of-birth record/by-birth-date)))
      (prn "Sorted by last-name, descending")
      (pprint/print-table (->> @data
                               (sort-by :last-name record/by-last-name))))))

(ns simple-app.core)
(require '[clojure.walk :as walk])

(defn with-formated-dates
  "Given a data structure formats dates as `MM/dd/yyyy`"
  [data]
  (walk/postwalk (fn [x]
                   (if (= (class x)
                          java.util.Date)
                     (.format (java.text.SimpleDateFormat. "MM/dd/yyyy") x)
                     x))
                 data))

(defn -main [input-file]
  (let [data (atom [])]
    (process-file-by-lines input-file record/parse (partial swap! data conj))
    (prn "Validating data")
    (s/explain :simple-app/records @data)
    (when (s/valid? :simple-app/records @data)
      (prn "Sorted by gender, then last-name")
      (pprint/print-table (->> @data
                               (sort-by :last-name)
                               (sort-by :gender record/by-gender)
                               (with-formated-dates)))
      (prn "Sorted by birth-date, ascending")
      (pprint/print-table (->> @data
                               (sort-by :date-of-birth record/by-birth-date)
                               (with-formated-dates)))
      (prn "Sorted by last-name, descending")
      (pprint/print-table (->> @data
                               (sort-by :last-name record/by-last-name)
                               (with-formated-dates))))))
