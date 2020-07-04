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
