(ns os-app.common)

(defn ->objective
  [i o]
  {:index i
   :status :off
   :player nil
   :color "#000000"})

(defn ->objectives
  [os]
  (map ->objective (range) os))

(def missions
  [{:id :incisive-attack
    :name "Incisive Attack"
    :size :combat-patrol
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :outriders
    :name "Outriders"
    :size :combat-patrol
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :encircle
    :name "Encircle"
    :size :combat-patrol
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :divide-and-conquer
    :name "Divide and Conquer"
    :size :incursion
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :crossfire
    :name "Crossfire"
    :size :incursion
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :centre-ground
    :name "Centre Ground"
    :size :incursion
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :forward-push
    :name "Forward Push"
    :size :incursion
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :ransack
    :name "Ransack"
    :size :incursion
    :objectives (->objectives [[]
                               []
                               []
                               []
                               []
                               []])}
   {:id :shifting-front
    :name "Shifting Front"
    :size :incursion
    :objectives (->objectives [[]
                               []
                               []
                               []])}])
