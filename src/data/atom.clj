(ns data.atom
  (:use [data.zero]))


(defn drain!
  "Returns the value of *zeroable* and 'zeros' it as per the Zeroable protocol"
  [*zeroable*]
  (let [old-value @*zeroable*]
    (swap! *zeroable* zero)))
