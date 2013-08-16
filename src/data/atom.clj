(ns data.atom
  (:use [data.zero]))


(defn drain! [*zeroable*]
  "Returns the value of *zeroable* and 'zeros' it as per the Zeroable protocol"
  (let [old-value @*zeroable*]
    (swap! *zeroable* zero)))
