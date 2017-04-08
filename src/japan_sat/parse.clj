(ns japan-sat.parse
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(defn- parse-puzzle
  [[horz, vert]]
  {:horz horz :vert vert})

(defn read-puzzle
  [resource]
  (-> resource
      io/resource
      slurp
      json/read-str
      parse-puzzle))
