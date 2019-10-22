(ns collections-musescore.views.score-views
  (:require ["@material-ui/core" :as mui]
            ["@material-ui/core/colors" :refer [red-js]]
            ["@material-ui/icons" :as mui-icons]
            [collections-musescore.views.inputs :as inputs]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn- score-info-item [icon text]
  [:span {:className "score-info-item"}
   [:> icon]
   text])

(defn trunc
  [s n]
  (if (> (count s) n)
    (str (apply str (take n s)) "...")
    s))

(defn score-view [collection-id {:keys [id title permalink
                                        favoriting_count
                                        view_count
                                        comment_count]
                                 {username :username} :user}]
  [:> mui/Card {:className "score-view" :style {:display "inline-block"}}
   [:> mui/CardContent {:className "score-content":style {:max-width 200}}
    [:> mui/Typography {:variant "h6" :gutterBottom true}
     (trunc title 20)]
    [:> mui/Typography {:component "div" :variant "body1"}
     [:> mui/Grid {:container true :spacing 1}
      [:> mui/Grid {:item true :xs 6} [:strong username]]
      [:> mui/Grid {:item true :xs 6}
       [score-info-item mui-icons/FavoriteBorder favoriting_count]]
      [:> mui/Grid {:item true :xs 6}
       [score-info-item mui-icons/VisibilityOutlined view_count]]
      [:> mui/Grid {:item true :xs 6}
       [score-info-item mui-icons/QuestionAnswer comment_count]]]]]

   [:> mui/CardActions {:disable-spacing false}
    [:a {:href permalink :target "_blank"}
     [:> mui/Button
      {:color "primary"}
      "Go to score"]]
    [:> mui/Button
     {:color "secondary"
      :className "pull-right"
      :on-click
      #(dispatch [:remove-score collection-id id])}
     "DELETE"]]])

(defn- get-suggestion-value [suggestion]
  (.-permalink suggestion))

(defn score-search-form []
  (let [url-info (subscribe [:url-info])]
    (fn [collection-id]
      [:div {:className "autosuggest"}
       [:div [inputs/auto-suggest-view {:placeholder "Type  stuff"
                                        :get-suggestion-value get-suggestion-value
                                        :suggestions @(subscribe [:suggestions])
                                        :update-suggestions
                                        #(dispatch [:get-suggestions (.-value %)])
                                        :on-suggestion-selected
                                        #(dispatch   [:get-url-info (.-suggestionValue %2)])
                                        :clear-suggestions #(dispatch [:clear-suggestions])}]]
       [:div {:style {:padding :20px}} [score-view 1 @url-info]]
       [:> mui/Button {:variant "contained"
                       :color "primary"
                       :on-click #(dispatch [:add-score collection-id @url-info])} "ADD"]])))

(defn add-score-modal [collection-id]
  (let [open? (reagent/atom false)]
    (fn [collection-id]
      [:div
       [:> mui/Button {:variant "contained"
                       :on-click #(reset! open? true)} "Open modal add score"]
       [:> mui/Modal {:on-close #(reset! open? false)
                      :open @open?}
        [:> mui/Paper {:className "add-score-modal"}
         [score-search-form collection-id]]]])))
