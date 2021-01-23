(ns objective-secured.core)

(defn turn-on
  [{:keys [mission-id objectives player-color]}]
  (mapv (fn [o]
          (prn "ON :" mission-id o player-color))
        objectives)
  {:status 200})

(defn turn-off
  [{:keys [mission-id objectives]}]
  (mapv (fn [o]
          (prn "OFF:" mission-id o))
        objectives)
  {:status 200})
