(ns poggio.functions.list
  (:use [poggio.functions core value]))

(def cons* (fn->pog-fn cons "cons" ["head" "tail"]))

(def head* (fn->pog-fn first "head" ["xs"]))

(def tail* (fn->pog-fn rest "tail" ["xs"]))

(def empty-list* (constantly* (list)))

(def map* (seq->pog-fn "map" ["f" "xs"]
            (list if* (list equals* "xs" empty-list*)
                  empty-list*
                  (list cons* (list "f" (list head* "xs"))
                              (list "map" "f" (list tail* "xs"))))))
