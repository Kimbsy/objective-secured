(ns objective-secured.arduino
  (:require [serial.core :as s]))

(def port-location "/dev/ttyACM0")
(defonce port (s/open port-location))

(defn prepare-string
  [s]
  (->> s (map byte) byte-array))

(defn send-command
  [action mission-id objective-index player-color]
  (->> [action mission-id objective-index player-color "\n"]
       (filter some?)
       (interpose "|")
       (apply str)
       prepare-string
       (s/write port)))
