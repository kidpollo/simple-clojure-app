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
