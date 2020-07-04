;; Brave Clojure - Vampire Data Analysis Program for the FWPD
;; To run with REPL:
;;   lein repl
;;   (load-file "src/fwpd/core.clj")
;; OR
;;   create a main function and call
;;   lein run

(ns fwpd.core)
(def filename "src/fwpd/suspects.csv")

; to print file content
; (slurp filename)

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))


(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))


(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]] ; reduce function is being used to "convert" each vector of the seq into a map
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row))) ; creates a seq of key-value pairs like ([:name "Bella Swan"] [:glitter-index 0])
       rows))

; retrive the first mapifyed line
;(first (mapify (parse (slurp filename))))

(defn glitter-filter
  "Filters out those with a :glitter-index less than the provided minimum-glitter"
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

; calling the functions to filter the data
(glitter-filter 3 (mapify (parse (slurp filename))))

;; EXERCISES

; return only the filtered names (not the whole record)
(defn glitter-filter-name
  "Filters out those with a :glitter-index less than the provided minimum-glitter"
  [minimum-glitter records]
  (map :name (filter #(>= (:glitter-index %) minimum-glitter) records)))

(glitter-filter-name 3 (mapify (parse (slurp filename))))


; append new suspect to the list
(defn append-suspect
  "Append a new suspect to the list"
  [suspect-list suspect]
  (conj suspect-list suspect))

(def suspect-list (mapify (parse (slurp filename))))

(def new-suspect {:name "Teddy Bear"
                  :glitter-index 2})

(append-suspect suspect-list new-suspect)

; function to validate record
(defn valid-record?
  "Returns nil if the record is invalid"
  [record]
  (and (:name record)
       (:glitter-index record)
       record))

; append new suspect to the list
(defn append-valid-suspect
  "Append only valid suspect to the list, otherwise return nil"
  [suspect-list suspect]
  (and (valid-record? suspect)
       (conj suspect-list suspect)))

; (valid-record? new-suspect)
; (append-valid-suspect suspect-list new-suspect)
(append-valid-suspect suspect-list 2)

; convert a list of maps to a CSV string
(defn tocsv
  "Convert list of suspects to CSV string"
  [suspects]
  (clojure.string/join "\n"
    (map (fn [suspect]
            (clojure.string/join "," (map suspect vamp-keys)))
        suspects)))

(tocsv suspect-list)
