(ns control.assert)

(defmulti error-message (fn [f & _] f))

(defmacro assert! [[f & args :as form]]
  "Same as assert, except uses the error message given by applying
   error-message to the function."
  `(let [f# ~f
         args# [~@args]]
     (when-not (apply f# args#)
       (throw (Exception. (error-message f# args#))))))

(defmethod error-message not-empty [f [list]]
  "Empty list.")
