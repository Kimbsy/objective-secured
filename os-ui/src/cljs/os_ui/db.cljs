(ns os-ui.db
  (:require [os-ui.common :as common]))

(def default-db
  {:mission-idx 0
   :mission (->> common/missions
                 (filter #(= :incisive-attack (:id %)))
                 first)
   :colors {:player-1 "#088DA5"
            :player-2 "#8B0000"}})
