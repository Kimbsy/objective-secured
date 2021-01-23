(ns objective-secured.core
  (:require [objective-secured.arduino :as a]))

(defn turn-on
  [{:keys [mission-id objectives player-color]}]
  (doseq [o objectives]
    (prn "ON" mission-id o player-color)
    (a/send-command "ON"
                    (name mission-id)
                    o
                    (clojure.string/upper-case player-color)))
  {:status 200})

(defn turn-off
  [{:keys [mission-id objectives]}]
  (doseq [o objectives]
    (prn "OFF" mission-id o)
    (a/send-command "OFF"
                    (name mission-id)
                    o
                    nil))
  {:status 200})
