(ns objective-secured.core)

(defn turn-on
  [{:keys [mission-id objective-index player-color]}]
  (prn "ON :" mission-id objective-index player-color)
  {:status 200})

(defn turn-off
  [{:keys [mission-id objective-index]}]
  (prn "OFF:" mission-id objective-index)
  {:status 200})
