(ns nifty-clj.elements
  (:import [de.lessvoid.nifty.controls Button DropDown ScrollPanel]
           [de.lessvoid.nifty.elements Element] 
           [de.lessvoid.nifty.elements.render TextRenderer]))

(defmulti nifty-control-class identity)

(defmethod nifty-control-class :default [control-class] control-class)

(defmethod nifty-control-class :button [_]
  Button)

(defmethod nifty-control-class :drop-down [_]
  DropDown)

(defmethod nifty-control-class :scroll [_]
  ScrollPanel)

(defn nifty-control [element control]
  (.getNiftyControl element (nifty-control-class control)))

(defn text [element]
  (-> element
      (.getRenderer TextRenderer)
      (.getWrappedText)))

(defn set-text! [element text]
  (-> element
      (.getRenderer TextRenderer)
      (.setText text)))

(defn get-button-text! [element]
  (-> element
      (nifty-control :button)
      (.getText)))

(defn set-button-text! [element text]
  (-> element
     (nifty-control :button)
     (.setText text)))

(defn first-child [^Element element]
  "Returns the first child under element."
  (.get (.getElements element) 0))

(defn remove-element! [^Element element]
  "Removes the element from its parent."
  (.markForRemoval element)
  (.layoutElements (.getParent element)))

(defn remove-children! [element children]
  "Removes every child in children from element."
  (doseq [child children]
    (.markForRemoval child))
  (.layoutElements element))

(defn remove-all-children! [^Element element]
  "Removes every child element under element."
  (remove-children! element (.getElements element)))

(def space->space-fn
  {:self (comp list identity)
   :parent (comp list #(.getParent %))
   :siblings #(.getElements (.getParent %))})

(defn select 
  "Selects elements from element based on the selector.
   If the selector is a string, it returns the first element
   whose id matches the string.
   If the selector is a regex pattern, it returns a seq of elemnts
   whose id matches the regex.
   Can optionally pass a 'space' keyword, which defines what space to
   perform our selection."
  ([element selector]
   (select element :self selector))
  ([element space selector]
    (let [space ((space->space-fn space) element)]
      (cond (string? selector) (some #(.findElementByName % selector)
                                     space)
            :else              (for [subspace space
                                     element (.getElements subspace)
                                     :when (re-find selector (.getId element))]
                                 element)))))



