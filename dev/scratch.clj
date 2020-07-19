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
