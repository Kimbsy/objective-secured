(ns os-app.db
  (:require [clojure.spec.alpha :as s]
            [os-app.common :as common]))

;; spec of app-db
(s/def ::mission-idx number?)
(s/def ::app-db
  (s/keys :req-un [::mission-idx]))

;; @TODO: spec out the db properly

;; initial state of app-db
(defonce app-db {:current-page :main
                 :mission-idx 0
                 :mission (->> common/missions
                               (filter #(= :incisive-attack (:id %)))
                               first)
                 :colors {:player-1 "#00FFFF"
                          :player-2 "#FF0000"}
                 :network {:ip-address "192.168.178.58"
                           :port "3000"}})
