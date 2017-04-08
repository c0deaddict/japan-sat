(ns japan-sat.print
  (:require [rolling-stones.core :as sat]
            [clojure.string :as s]))

; Unwrap the negative from the result to expose the coordinate.
; https://github.com/Engelberg/rolling-stones
(defn get-coord
  [result]
  (if (sat/negative? result)
    (sat/negate result)
    result))

(defn get-y
  [result]
  (second (get-coord result)))

(defn get-x
  [result]
  (first (get-coord result)))

(defn parse-result-row
  [results row-index]
  (s/join
    (map #(if (sat/positive? %) "##" "  ")
         (sort-by get-x (filter #(== (get-y %) row-index) results)))))

(defn parse-result
  [results height]
  (s/join "\n"
          (map #(parse-result-row results %) (range 0 height))))
