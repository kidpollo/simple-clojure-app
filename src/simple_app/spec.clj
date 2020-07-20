(ns simple-app.specs
  (:require [clojure.spec.alpha :as s]))

(s/def :simple-app/last-name string?)
(s/def :simple-app/first-name string?)
(s/def :simple-app/gender string?)
(s/def :simple-app/favorite-color string?)
(s/def :simple-app/date-of-birth inst?)

(s/def :simple-app/record (s/keys :req-un [:simple-app/first-name
                                           :simple-app/last-name
                                           :simple-app/gender
                                           :simple-app/favorite-color
                                           :simple-app/date-of-birth]))

(s/def :simple-app/records (s/coll-of :simple-app/record))
