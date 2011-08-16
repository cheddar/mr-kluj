(use     '(simple-avro schema))
(require '[com.linkedin.mr-kluj.job :as job]
         '[com.linkedin.mr-kluj.avro-storage :as avro])

; Output schema 
(defavro-record NameCount
	:first avro-string
	:count avro-int)
  
(job/run 
  (job/staged-job ["avro-job" "staging-location"]
    (avro/avro-storage-input "test.avro")
    (job/map-mapper (fn [key value context] [[(get value "first") 1]]))
    (job/create-reducer (fn [key values context] [[nil {"first" key "count" (count values)}]]))
    (avro/avro-intermediate-data avro-string avro-int)
    (avro/avro-storage-output "count.avro" nil NameCount)))
  