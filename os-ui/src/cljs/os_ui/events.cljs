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

(defn -current-objective-indices
  [objective db]
  (if (= :*ALL* (:index objective))
    (->> db
         :mission
         :objectives
         (map :index))
    [(:index objective)]))

(re-frame/reg-event-db
 ::turn-on
 (fn [db [_ mission objective player player-color]]
   (let [objective-indices (-current-objective-indices objective db)]
     (http/post "http://192.168.0.66:3000/turn-on"
                {:with-credentials? false
                 :json-params {:mission-id mission
                               :objectives objective-indices
                               :player-color player-color}})
     (update-in db [:mission :objectives]
                (fn [os]
                  (->> (concat (remove #((set objective-indices) (:index %)) os)
                               (->> (filter #((set objective-indices) (:index %)) os)
                                    (map (fn [o]
                                           (-> o
                                               (assoc :player player)
                                               (assoc :color player-color))))))
                       (sort-by :index)))))))

(re-frame/reg-event-db
 ::turn-off
 (fn [db [_ mission objective]]
   (let [objective-indices (-current-objective-indices objective db)]
     (http/post "http://192.168.0.66:3000/turn-off"
                {:with-credentials? false
                 :json-params {:mission-id mission
                               :objectives objective-indices}})
     (update-in db [:mission :objectives]
                (fn [os]
                  (->> (concat (remove #((set objective-indices) (:index %)) os)
                               (->> (filter #((set objective-indices) (:index %)) os)
                                    (map (fn [o]
                                           (assoc o :player nil)))))
                       (sort-by :index)))))))

(defn -held-objective-indices
  [player db]
  (->> db
       :mission
       :objectives
       (filter #(= player (:player %)))
       (map :index)))

(re-frame/reg-event-db
 ::update-color
 (fn [{:keys [colors] :as db} [_ mission player color]]
   (http/post "http://192.168.0.66:3000/turn-on"
              {:with-credentials? false
               :json-params {:mission-id (:id mission)
                             :objectives (-held-objective-indices player db)
                             :player-color color}})
   (assoc db :colors (assoc colors player color))))
