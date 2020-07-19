(ns simple-app.record-test
  (:require [simple-app.record :as record]
            [clojure.test :refer [deftest testing is]]))

(deftest parse-test
  (let [expected {:first-name "foo"
                  :last-name "bar"
                  :gender "neutral"
                  :favorite-color "chartreuse"
                  :date-of-birth (java.util.Date. "1/1/2020")}
        pipe-format "foo | bar | neutral | chartreuse | 1/1/2020"
        comma-format "foo, bar, neutral, chartreuse, 1/1/2020"
        space-format "foo bar neutral chartreuse 1/1/2020"]

    (testing "Pipe format"
      (is (= (record/parse pipe-format) expected)))
    (testing "Comma format"
      (is (= (record/parse comma-format) expected)))
    (testing "Space format"
      (is (= (record/parse space-format) expected)))))
