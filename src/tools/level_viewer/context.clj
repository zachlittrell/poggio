(ns tools.level-viewer.context)

(defprotocol LevelContext
  (add-end-level-watch! [context watch])
  (end-level! [context complete?]))
