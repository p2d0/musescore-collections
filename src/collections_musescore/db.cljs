(ns collections-musescore.db)

(def ls-key "collections-musescore")                         ;; localstore key

(def default-db           ;; what gets put into app-db by default.
  {:collections   (sorted-map)  ;; an empty list of todos. Use the (int) :id as the key
   }) 

(defn ->local-store
  "Puts todos into localStorage"
  [todos]
  (.setItem js/localStorage ls-key (str todos)))     ;; sorted-map written as an EDN map

(re-frame/reg-cofx
  :local-store-collections
  (fn [cofx _]
      ;; put the localstore todos into the coeffect under :local-store-todos
      (assoc cofx :local-store-collections
             ;; read in todos from localstore, and process into a sorted map
             (into (sorted-map)
                   (some->> (.getItem js/localStorage ls-key)
                            (cljs.reader/read-string)    ;; EDN map -> map
                            )))))