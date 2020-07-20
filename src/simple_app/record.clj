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

(ns simple-app.record)
(defn by-gender
  "Gender comparator function"
  [a b]
  (compare a b))

(defn by-birth-date
  "Gender comparator function"
  [a b]
  (compare a b))

(defn by-last-name
  "Last Name comparator function"
  [a b]
  (compare a b))

(ns simple-app.record)
(defn by-gender
  "Gender comparator function"
  [a b]
  (cond
    (= a b)
    0
    (= a "female")
    -1
    (= b "female")
    1
    (and (= a "male") (not= b "female"))
    -1
    (and (= b "male") (not= a "female"))
    1))

(defn by-birth-date
  "Gender comparator function"
  [a b]
  (compare a b))

(defn by-last-name
  "Last Name comparator function"
  [a b]
  (compare b a))

(ns simple-app.record)
(defn by-last-name
  "Last Name comparator function"
  [a b]
  (compare (str/upper-case b) (str/upper-case a)))

(ns simple-app.record)
(require '[clojure.string :as str])
(require '[clojure.spec.alpha :as s])
(require '[simple-app.spec])

(defn parse
  "Parses input line into a record entry map.
  Handles 3 different input formats.
    - Comma separated
    - Pipe separated
    - Space separated"
  [line]
  (let [validate (fn [entry]
                   (when (s/valid? :simple-app/record entry) entry))]
    (-> (zipmap [:first-name :last-name :gender :favorite-color :date-of-birth]
                (str/split line #",\s+|\s+\|\s+|\s+"))
        (update :date-of-birth #(try (java.util.Date. %) (catch Exception _ nil)))
        (validate))))
