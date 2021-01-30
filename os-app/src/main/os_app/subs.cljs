(ns os-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::mission
 (fn [db]
   (:mission db)))

(reg-sub
 ::objectives
 (fn [db]
   (get-in db [:mission :objectives])))

(reg-sub
 ::colors
 (fn [db]
   (:colors db)))
