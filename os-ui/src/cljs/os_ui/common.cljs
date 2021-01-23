(ns os-ui.common)

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
    :image "../img/incisive-attack.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :outriders
    :name "Outriders"
    :size :combat-patrol
    :image "../img/outriders.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :encircle
    :name "Encircle"
    :size :combat-patrol
    :image "../img/encircle.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :divide-and-conquer
    :name "Divide and Conquer"
    :size :incursion
    :image "../img/divide-and-conquer.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :crossfire
    :name "Crossfire"
    :size :incursion
    :image "../img/crossfire.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :centre-ground
    :name "Centre Ground"
    :size :incursion
    :image "../img/centre-ground.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :forward-push
    :name "Forward Push"
    :size :incursion
    :image "../img/forward-push.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}
   {:id :ransack
    :name "Ransack"
    :size :incursion
    :image "../img/ransack.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []
                               []
                               []])}
   {:id :shifting-front
    :name "Shifting Front"
    :size :incursion
    :image "../img/shifting-front.jpg"
    :objectives (->objectives [[]
                               []
                               []
                               []])}])
