(ns os-app.events
  (:require
   [re-frame.core :refer [reg-event-db after]]
   [clojure.spec.alpha :as s]
   [os-app.common :as common]
   [os-app.db :as db :refer [app-db]]
   [cljs-http.client :as http]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 :initialize-db
 validate-spec
 (fn [_ _]
   app-db))

(reg-event-db
 ::prev-mission
 (fn [{:keys [mission-idx] :as db} _]
   (let [new-idx (mod (dec mission-idx) (count common/missions))]
     (-> db
         (assoc :mission-idx new-idx)
         (assoc :mission (nth common/missions new-idx))))))

(reg-event-db
 ::next-mission
 (fn [{:keys [mission-idx] :as db} _]
   (let [new-idx (mod (inc mission-idx) (count common/missions))]
     (-> db
         (assoc :mission-idx new-idx)
         (assoc :mission (nth common/missions new-idx))))))

(reg-event-db
 ::page
 (fn [db [_ page]]
   (prn "paging")
   (assoc db :current-page page)))

(defn -current-objective-indices
  [objective db]
  (if (= :*ALL* (:index objective))
    (->> db
         :mission
         :objectives
         (map :index))
    [(:index objective)]))

(reg-event-db
 ::turn-on
 (fn [db [_ mission objective player player-color]]
   (let [objective-indices (-current-objective-indices objective db)]
     (http/post "http://192.168.0.77:3000/turn-on"
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

(reg-event-db
 ::turn-off
 (fn [db [_ mission objective]]
   (let [objective-indices (-current-objective-indices objective db)]
     (http/post "http://192.168.0.77:3000/turn-off"
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

(reg-event-db
 ::update-color
 (fn [{:keys [colors] :as db} [_ mission player color]]
   (http/post "http://192.168.0.77:3000/turn-on"
              {:with-credentials? false
               :json-params {:mission-id mission
                             :objectives (-held-objective-indices player db)
                             :player-color color}})
   (assoc db :colors (assoc colors player color))))
