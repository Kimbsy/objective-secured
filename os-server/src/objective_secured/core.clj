(ns objective-secured.core
  (:require [objective-secured.arduino :as a]))

(defn flash-startup
  []
  (doseq [o (range 4)]
    (a/send-command "ON" "incisive-attack" o "#FFFFFF"))
  (Thread/sleep 500)
  (doseq [o (range 4)]
    (a/send-command "OFF" "incisive-attack" o nil)))

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
  (prn (str "TURNING OFF:" mission-id objectives))
  (doseq [o objectives]
    (a/send-command "OFF"
                    (name mission-id)
                    o
                    nil))
  {:status 200})
