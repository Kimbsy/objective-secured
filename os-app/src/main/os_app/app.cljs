(ns os-app.app
  (:require
   ["react-native" :as rn]
   [reagent.core :as r]
   [re-frame.core :as rf]
   [shadow.expo :as expo]
   [os-app.events :as e]
   [os-app.subs :as s]))

;; must use defonce and must refresh full app so metro can fill these in
;; at live-reload time `require` does not exist and will cause errors
;; must use path relative to :output-dir

(defonce splash-img (js/require "../assets/shadow-cljs.png"))
(defonce deployment-maps
  {:incisive-attack (js/require "../assets/deployment-maps/incisive-attack.jpg")
   :outriders (js/require "../assets/deployment-maps/outriders.jpg")
   :encircle (js/require "../assets/deployment-maps/encircle.jpg")
   :divide-and-conquer (js/require "../assets/deployment-maps/divide-and-conquer.jpg")
   :crossfire (js/require "../assets/deployment-maps/crossfire.jpg")
   :centre-ground (js/require "../assets/deployment-maps/centre-ground.jpg")
   :forward-push (js/require "../assets/deployment-maps/forward-push.jpg")
   :ransack (js/require "../assets/deployment-maps/ransack.jpg")
   :shifting-front (js/require "../assets/deployment-maps/shifting-front.jpg")})

(def styles
  {:main             {:flex             1
                      :background-color :white
                      :align-items      :center
                      :justify-content  :flex-start
                      :padding-top      50}
   :container        {:flex             1
                      :background-color :white
                      :align-items      :center
                      :justify-content  :flex-start}
   :mission-row      {:flex-direction   :row
                      :align-self       :stretch
                      :justify-content  :space-around
                      :width            380}
   :objective-row    {:flex-direction   :row
                      :align-self       :stretch
                      :justify-content  :space-around
                      :width            380}
   :mission-button   {:font-weight      :bold
                      :font-size        18
                      :padding          6
                      :background-color :blue
                      :border-radius    999
                      :margin-bottom    20}
   :objective-button {:font-weight      :bold
                      :font-size        18
                      :padding          6
                      :border-radius    999
                      :margin-bottom    20
                      :background-color :gray}
   :button-text      {:padding-left  12
                      :padding-right 12
                      :font-weight   :bold
                      :font-size     18
                      :color         :white}
   :deployment-map   {:width  380
                      :height 250}
   :creds            {:font-weight :normal
                      :font-size   15
                      :color       :blue}})

(defn mission
  []
  [:> rn/View {:style (merge (:container styles)
                             {
                              :max-height "42%"})}
   [:> rn/Image {:style  (:deployment-map styles)
                 :source (-> @(rf/subscribe [::s/mission])
                             :id
                             deployment-maps)}]
   [:> rn/View {:style (merge (:mission-row styles)
                              {:padding-top 10})}
    [:> rn/TouchableOpacity {:style    (:mission-button styles)
                             :on-press #(rf/dispatch [::e/prev-mission])}
     [:> rn/Text {:style (:button-text styles)} "<"]]
    [:> rn/Text {:style (:counter styles)} (-> @(rf/subscribe [::s/mission])
                                               :id)]
    [:> rn/TouchableOpacity {:style    (:mission-button styles)
                             :on-press #(rf/dispatch [::e/next-mission])}
     [:> rn/Text {:style (:button-text styles)} ">"]]]])

(defn row
  [mission {:keys [index] :as objective} {p1-color :player-1 p2-color :player-2}]
  [:> rn/View {:style (:objective-row styles)}
   [:> rn/Text {:style (merge (:button-text styles)
                              {:color :black})}
    (if (= :*ALL* index)
      "All Objectives"
      (str "Objective " (:index objective)))]
   [:> rn/View {:style (merge (:objective-row styles)
                              {:flex 1})}
    [:> rn/TouchableOpacity {:style    (merge (:objective-button styles)
                                              {:background-color p1-color})
                             :on-press #(rf/dispatch [::e/turn-on (:id mission) objective :player-1 p1-color])}
     [:> rn/Text {:style (:button-text styles)} "+"]]
    [:> rn/TouchableOpacity {:style    (:objective-button styles)
                             :on-press #(rf/dispatch [::e/turn-off (:id mission) objective])}
     [:> rn/Text {:style (:button-text styles)} "-"]]
    [:> rn/TouchableOpacity {:style    (merge (:objective-button styles)
                                              {:background-color p2-color})
                             :on-press #(rf/dispatch [::e/turn-on (:id mission) objective :player-2 p2-color])}
     [:> rn/Text {:style (:button-text styles)} "+"]]]])

(defn objective-controls
  []
  (let [mission @(rf/subscribe [::s/mission])
        objectives @(rf/subscribe [::s/objectives])
        colors @(rf/subscribe [::s/colors])]
    [:> rn/View {:style (merge (:container styles)
                               {:background-color :orage})}
     (for [objective (concat objectives [{:index :*ALL*}])]
       ^{:key objective} [row mission objective colors])
     ]))

(defn player-controls
  []
  [:<>])

(defn root []
  [:> rn/View {:style (:main styles)}
   [mission]
   [objective-controls]
   [player-controls]])

(defn start
  {:dev/after-load true}
  []
  (expo/render-root (r/as-element [root])))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (start))
