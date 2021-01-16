(ns os-ui.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::mission
 (fn [db]
   (:mission db)))

(re-frame/reg-sub
 ::objectives
 (fn [db]
   (get-in db [:mission :objectives])))

(re-frame/reg-sub
 ::colors
 (fn [db]
   (:colors db)))
