(ns objective-secured.arduino
  (:require [serial.core :as s]
            [clojure.java.shell :as shell]))

;; (def port-location "/dev/ttyACM0")
;; (def port (s/open port-location))

(defn prepare-string
  [s]
  (->> s (map byte) byte-array))

(defn send-command
  [action mission-id objective-index player-color]
  (let [command-string (->> [action mission-id objective-index player-color "\n"]
                            (filter some?)
                            (interpose "|")
                            (apply str))]
    (shell/sh "python3" "../buffer.py" command-string)))
