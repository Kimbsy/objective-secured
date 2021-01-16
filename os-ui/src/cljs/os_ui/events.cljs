(ns os-ui.events
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [re-frame.core :as re-frame]
   [os-ui.common :as common]
   [os-ui.db :as db]
   [cljs.core.async :refer [<!]]
   [cljs-http.client :as http]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::prev-mission
 (fn [{:keys [mission-idx] :as db} _]
   (let [new-idx (mod (dec mission-idx) (count common/missions))]
     (-> db
         (assoc :mission-idx new-idx)
         (assoc :mission (nth common/missions new-idx))))))

(re-frame/reg-event-db
 ::next-mission
 (fn [{:keys [mission-idx] :as db} _]
   (let [new-idx (mod (inc mission-idx) (count common/missions))]
     (-> db
         (assoc :mission-idx new-idx)
         (assoc :mission (nth common/missions new-idx))))))

(re-frame/reg-event-fx
 ::turn-on
 (fn [{:keys [db]} [_ mission objective player-color]]
   (prn "ON" mission objective player-color)
   (http/post "http://192.168.0.66:3000/turn-on"
              {:with-credentials? false
               :json-params {:mission-id mission
                             :objective-index (:index objective)
                             :player-color player-color}})))

(re-frame/reg-event-fx
 ::turn-off
 (fn [{:keys [db]} [_ mission objective]]
   (prn "OFF" mission objective)
   (http/post "http://192.168.0.66:3000/turn-off"
              {:with-credentials? false
               :json-params {:mission-id mission
                             :objective-index (:index objective)}})))

(re-frame/reg-event-db
 ::update-color
 (fn [{:keys [colors] :as db} [_ player color]]
   (assoc db :colors (assoc colors player color))))
