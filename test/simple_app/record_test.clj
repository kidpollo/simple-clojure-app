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

(ns simple-app.record-test)
(deftest sort-tests
  (testing "by-gender"
    (is (= 0 (record/by-gender "female" "female"))
        "same")
    (is (= -1 (record/by-gender "female" "male"))
        "female before male")
    (is (= 1 (record/by-gender "male" "female"))
        "male after female")

    (is (= -1 (record/by-gender "female" "other"))
        "female before other")
    (is (= 1 (record/by-gender "other" "female"))
        "other after female")

    (is (= -1 (record/by-gender "male" "other"))
        "male before other")
    (is (= 1 (record/by-gender "other" "male"))
        "other after male"))
  (testing "birth-date"
    (is (= 0 (record/by-birth-date (java.util.Date. "1/1/2020")
                                   (java.util.Date. "1/1/2020")))
        "same")
    (is (= -1 (record/by-birth-date (java.util.Date. "1/1/2020")
                                    (java.util.Date. "1/2/2020")))
        "ascending")
    (is (= 1 (record/by-birth-date (java.util.Date. "1/2/2020")
                                   (java.util.Date. "1/1/2020")))))
  (testing "last-name"
    (is (= 0 (record/by-last-name "Viramontes" "Viramontes"))
        "same")
    (is (> 0 (record/by-last-name "Viramontes" "Jenkins"))
        "descending")
    (is (< 0 (record/by-last-name "Jenkins" "Viramontes")))))

(ns simple-app.record-test)
(deftest last-name-sort-case-tests
  (testing "last-name"
    (is (= 0 (record/by-last-name "Viramontes" "viramontes"))
        "same")
    (is (> 0 (record/by-last-name "Viramontes" "jenkins"))
        "descending")
    (is (< 0 (record/by-last-name "jenkins" "Viramontes")))))

(ns simple-app.record-test)
(deftest handle-bad-data-test
  (let [expected nil
        bad-date "foo | bar | neutral | chartreuse | foo"
        empty-data ""
        weird-data "klj;a f``jsaldkf asld kflask dfjl;kasdl;kfjaslkdfjl;kasd  aslk jflk;a sdl;kfjs"]

    (testing "Bad date"
      (is (= (record/parse bad-date) expected)))
    (testing "Empty data"
      (is (= (record/parse empty-data) expected)))
    (testing "Wat?"
      (is (= (record/parse weird-data) expected)))))
