; Contains a main function and the first exercises of Brave Clojure (Chap 3)

(ns clojure-noob.core
  (:gen-class))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining        ; recur is like a "continue" that increments the values on the loop
               (into final-body-parts
                     (set [part (matching-part part)])))))))


; Just a tests for creating my own reduce function
(defn my-reduce
  ([f initial coll]
   (println (str "\ninitial " initial " - coll " coll ))
   (loop [result initial
          remaining coll]
     (if (empty? remaining)
       result
       (recur (f result (first remaining)) (rest remaining)))))
  ([f [head & tail]]
   (println (str "\nhead " head " - tail " tail ))
   (my-reduce f head tail)))

; re-implement the symmetrize-body-parts using "reduce" function
; reduce usage: (reduce + [1 2]) or (reduce + 3 [1 2])
(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)]))) ; function
          []                                                           ; initial value
          asym-body-parts))                                            ; vector of values to apply the function


; Hit the hobbit according to the random target (size of sum of parts)
(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)  ; create the symetric parts from asymetric parts
        body-part-size-sum (reduce + (map :size sym-parts))       ; calc the sum of the size of all parts
        target (rand body-part-size-sum)]                         ; radomly choose the target size
    (println (str "\n  body-sum: " body-part-size-sum " target: " target))
    (loop [[part & remaining] sym-parts                           ; loop to hit the first part in the list while accumulated-size <= target
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        (do (println (str "\n  size: " accumulated-size " target: " target))
            part)                                                 ; this line just return the last part (don't know why)
        (do (println (str "\n ac-size: " accumulated-size " first-remaining size: " (:size (first remaining))))
            (recur remaining                                      ; continue the loop "incrementing" for remaining and accumulated-size
                   (+ accumulated-size (:size (first remaining)))))))))

; some exercises
(defn add100
  "This function adds 100 to a number"
  [number]
  (+ 100 number)
  )

(defn dec-maker
  "Create a custom decrementor"
  [dec-by]
  #(- % dec-by))


(defn better-symmetrize-body-partsa
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)]))) ; function
          []                                                           ; initial value
          asym-body-parts))                                            ; vector of values to apply the function

(defn mapset
  "mapset function returns is like a map, but returns a hash-set"
  [f values]
  (set (map f values))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  ; prints asymetric hobbig
  (println "Asymetric Hobbit")
  (println asym-hobbit-body-parts)

  ; prints symetric hobbit
  ;; (println "\nSymetric Hobbit")
  ;; (println (symmetrize-body-parts asym-hobbit-body-parts))

  ; prints symetric hobbit
  (println "\nSymetric Hobbit 2")
  (println (better-symmetrize-body-parts asym-hobbit-body-parts))

  ; calls function to hit hobbit
  (println "\nHit Hobbit\n")
  (hit asym-hobbit-body-parts)

  ; exercises
  ; adds 100 to a number
  (println (str "\n add100 result: " (add100 2.34)))

  ; custom decrementor
  (def dec3 (dec-maker 3))
  (println (str "\n dec3 result: " (dec3 22)))

  ; my mapset
  (println (str "\n mapset applied " (mapset dec3 [6 7 8])))
  (println (str "\n mapset applied " (mapset inc [1 1 2 2]))))






