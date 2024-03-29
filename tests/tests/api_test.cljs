(ns tests.api-test
  (:require
   [cljs.test :refer-macros [async are is deftest testing]]
   [collections-musescore.api :as api]))

(def url "https://musescore.com/user/24625996/scores/4801654")

(deftest parse-url
  (is (= "4801654" (api/parse-url url))))

(defn are-scores-equal? [score1 score2]
  (let [keys [:title :description :tags]]
    (is (=
         (select-keys score1 keys)
         (select-keys score2 keys)))))

#_(deftest get-information-by-url-test
  (async done
         (api/get-info-by-url url (fn [result]
                                    (are-scores-equal? expected-response result)
                                    (done)))))

#_(deftest search-score-test
  (async done
         (api/search-score "Cool beans" (fn [result]
                                          (is (seq result))))
         (done)))
