(ns os-ui.views
  (:require
   [re-frame.core :as rf]
   [os-ui.events :as e]
   [os-ui.subs :as s]
   [hiccup-icons.octicons11 :as o]))

(defn mission
  []
  (let [mission @(rf/subscribe [::s/mission])]
    [:<>
     [:img {:src (:image mission)
            :style {:width "100%"
                    :object-fit "scale-down"}}]
     [:table {:style {:width "100%"
                      :margin-top "20px"
                      :margin-bottom "20px"}}
      [:tr
       [:td
        [:button.btn.btn-lg.btn-block.btn-warning
         {:style {:margin "0 5px"}
          :onClick #(rf/dispatch [::e/prev-mission])}
         o/chevron-left]]
       [:td {:style {:text-align "center"}}
        [:h2 (:name mission)]]
       [:td
        [:button.btn.btn-lg.btn-block.btn-warning
         {:style {:margin "0 5px"}
          :onClick #(rf/dispatch [::e/next-mission])}
         o/chevron-right]]]]]))

(defn objective-row
  [mission objective {p1-color :player-1 p2-color :player-2}]
  [:tr
   [:td {:style {:text-align "center"}}
    (:index objective)]
   [:td
    [:button.btn.btn-lg.btn-block
     {:style {:margin "0 5px"
              :background-color p1-color
              :color "#FFFFFF"}
      :onClick #(rf/dispatch [::e/turn-on (:id mission) objective p1-color])}
     o/sun]]
   [:td
    [:button.btn.btn-lg.btn-block.btn-secondary
     {:style {:margin "0 5px"}
      :onClick #(rf/dispatch [::e/turn-off (:id mission) objective])}
     o/horizontal-rule]]
   [:td
    [:button.btn.btn-lg.btn-block
     {:style {:margin "0 5px"
              :background-color p2-color
              :color "#FFFFFF"}
      :onClick #(rf/dispatch [::e/turn-on (:id mission) objective p2-color])}
     o/sun]]])

(defn objective-controls
  []
  (let [mission @(rf/subscribe [::s/mission])
        objectives @(rf/subscribe [::s/objectives])
        colors @(rf/subscribe [::s/colors])]
    [:table {:style {:width "100%"
                     :margin-bottom "20px"}}
     [:thead
      [:tr
       [:th {:style {:text-align "center"}}
        "Objective #"]
       [:th {:style {:text-align "center"}}
        "Player 1"]
       [:th {:style {:text-align "center"}}
        "Off"]
       [:th {:style {:text-align "center"}}
        "Player 2"]]
      (map #(objective-row mission % colors) objectives)]]))

(defn player-controls
  []
  (let [{p1-color :player-1 p2-color :player-2} @(rf/subscribe [::s/colors])]
    [:<>
     [:div.input-group.input-group-lg {:style {:margin-bottom "20px"}}
      [:div.input-group-prepend
       [:span.input-group-text {:style {:background-color p1-color
                                        :color "#FFFFFF"}}
        "Player 1"]]
      [:input.form-control {:type "text"
                            :placeholder p1-color
                            :on-change
                            (fn [x]
                              (let [val (-> x .-target .-value)]
                                (when (re-matches #"#[0-9a-fA-F]{6}" val)
                                  (rf/dispatch [::e/update-color :player-1 val]))))}]]
     [:div.input-group.input-group-lg {:style {:margin-bottom "20px"}}
      [:div.input-group-prepend
       [:span.input-group-text {:style {:background-color p2-color
                                        :color "#FFFFFF"}}
        "Player 2"]]
      [:input.form-control {:type "text"
                            :placeholder p2-color
                            :on-change
                            (fn [x]
                              (let [val (-> x .-target .-value)]
                                (when (re-matches #"#[0-9a-fA-F]{6}" val)
                                  (rf/dispatch [::e/update-color :player-2 val]))))}]]]))

(defn main-panel
  []
  [:div.container
   [:h1
    [:code "objective-secured"]]
   [:p [:i "> light 'em up!"]]
   (mission)
   (objective-controls)
   (player-controls)])
