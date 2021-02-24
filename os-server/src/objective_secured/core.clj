(ns objective-secured.core
  (:require [objective-secured.arduino :as a]))

(defn turn-on
  [{:keys [mission-id objectives player-color]}]
  (prn (str "TURNING ON:" mission-id objectives player-color))
  (doseq [o objectives]
    (a/send-command "ON"
                    (name mission-id)
                    o
                    (clojure.string/upper-case player-color)))
  {:status 200})

(defn turn-off
  [{:keys [mission-id objectives]}]
  (prn (str "TURNING OFF:" mission-id objecttives))
  (doseq [o objectives]
    (a/send-command "OFF"
                    (name mission-id)
                    o
                    nil))
  {:status 200})
