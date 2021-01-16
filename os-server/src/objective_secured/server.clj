(ns objective-secured.server
  (:gen-class)
  (:require [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.spec]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.cors :as cors]
            [objective-secured.core :as core]))

(defonce sys (atom nil))

(def app
  (->
   (ring/ring-handler
    (ring/router
     [["/swagger.json"
       {:get {:no-doc  true
              :swagger {:info {:title       "objective-secured"
                               :description "controlling 40k battlefield objective marker lights"}}
              :handler (swagger/create-swagger-handler)}}]
      ["/turn-on"
       {:post {:summary "turn on a light specified by mission and index"
               :parameters {:body map?}
               :handler (fn [{{:keys [body]} :parameters}]
                          (core/turn-on body))}}]
      ["/turn-off"
       {:post {:summary "turn off a light specified by mission and index"
               :parameters {:body map?}
               :handler (fn [{{:keys [body]} :parameters}]
                          (core/turn-off body))}}]]

     {:exception pretty/exception
      :data      {:coercion   reitit.coercion.spec/coercion
                  :muuntaja   m/instance
                  :middleware [swagger/swagger-feature
                               parameters/parameters-middleware
                               muuntaja/format-negotiate-middleware
                               muuntaja/format-response-middleware
                               exception/exception-middleware
                               muuntaja/format-request-middleware
                               coercion/coerce-response-middleware
                               coercion/coerce-request-middleware
                               multipart/multipart-middleware]}})
    (ring/routes
     (swagger-ui/create-swagger-ui-handler
      {:path   "/"
       :config {:validationUrl    nil
                :operationsSorter "alpha"}})
     (ring/create-default-handler)))
   (cors/wrap-cors
    :access-control-allow-origin  [#".*"]
    :access-control-allow-headers ["Content-Type"]
    :access-control-allow-methods [:post])))

(defn start
  []
  (reset! sys (jetty/run-jetty #'app {:port 3000 :join? false}))
  (println "server running on port 3000"))

(defn stop
  []
  (clojure.pprint/pprint @sys)
  (.stop @sys))

(defn restart
  []
  (stop)
  (start))

(defn -main
  "Start the server."
  [& _]
  (start))
