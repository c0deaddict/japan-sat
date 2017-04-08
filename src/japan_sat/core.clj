(ns japan-sat.core
  (:gen-class)
  (:require [rolling-stones.core :as sat :refer [! at-least at-most exactly AND OR NOT]]
            [clojure.string :as s]
            [japan-sat.print :refer [parse-result]])
  (:use [japan-sat.parse]))

(defn spec-permutations
  "All possible permutations of a spec in a row of the given width."
  ([spec width]
   (spec-permutations spec width 1 []))
  ([spec width offset acc]
   (if (empty? spec)
     [acc]
     (let [[x & xs] spec
           spacings (range offset (+ 2 (- width x)))]
       (mapcat #(spec-permutations xs width (+ % x 1) (conj acc %)) spacings)))))

(defn assoc-range
  [vec [offset length] value]
  (reduce #(assoc %1 (- %2 1) value) vec
          (range offset (+ offset length))))

(defn unfold-permutation
  "Unfold a permutation to a row of black (0) and white (1) pixels."
  [permutation spec dim]
  (let [blocks (map vector permutation spec)
        empty-row (into [] (repeat dim 0))]
    (reduce #(assoc-range %1 %2 1) empty-row blocks)))

(defn pixels->constraint
  "Map a row of pixels to a SAT constraint."
  [pixels get-cell]
  (apply AND (map-indexed
    (fn [idx color]
      (if (== color 0)
        (! (get-cell idx))
        (get-cell idx)))
    pixels)))

(defn unfold-spec
  "Unfold a spec into all possible pixels."
  [spec dim]
  (mapv #(unfold-permutation % spec dim)
        (spec-permutations spec dim)))

(defn unfold->constraint
  [unfolded-specs get-cell]
  (exactly 1 (map #(pixels->constraint % get-cell) unfolded-specs)))

(defn specs->constraints
  [specs dim get-cell]
  (map-indexed
    (fn [idx spec]
      (unfold->constraint (unfold-spec spec dim) (get-cell idx)))
    specs))

(defn puzzle->constraint
  [puzzle]
  (let [width (count (:vert puzzle))
        height (count (:horz puzzle))
        horz-cell (fn [y] (fn [x] [x y]))
        vert-cell (fn [x] (fn [y] [x y]))
        horz-constraint (specs->constraints (:horz puzzle) width horz-cell)
        vert-constraint (specs->constraints (:vert puzzle) height vert-cell)]
    (concat horz-constraint vert-constraint)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [puzzle (read-puzzle "puzzles/11.txt")
        constraints (puzzle->constraint puzzle)
        result (sat/solve-symbolic-formula constraints)
        num-solutions (count (sat/solutions-symbolic-formula constraints))
        height (count (:horz puzzle))]
    (println (str "Solutions: " num-solutions))
    (println (parse-result result height))))