(ns control.timer)

(defprotocol Timer
  "An object that acts like a Timer."
  (start! [timer] "Starts the timer. Not guarenteed to immediately go off.")
  (stop! [timer]) "Stops the timer.")
