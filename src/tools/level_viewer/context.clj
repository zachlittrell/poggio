(ns tools.level-viewer.context)

(defprotocol LevelContext
  (end-level! [context complete?]))
