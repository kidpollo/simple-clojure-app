(ns simple-app.record
  (:require [clojure.spec.alpha :as s]))

(defn parse [line] 
  {})

(ns simple-app.record)
(require '[clojure.string :as str])

(defn parse
  "Parses input line into a record entry map.
  Handles 3 different input formats.
    - Comma separated
    - Pipe separated
    - Space separated"
  [line]
  (-> (zipmap [:first-name :last-name :gender :favorite-color :date-of-birth]
              (str/split line #",\s+|\s+\|\s+|\s+"))
      (update :date-of-birth #(java.util.Date. %))))
