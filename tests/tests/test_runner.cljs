(ns tests.test-runner
  (:require [cljs.test :refer-macros [run-tests]]
            [tests.core]
            [tests.views-test]
            [tests.db-spec-test]
            [tests.db-test]
            [tests.api.api-test]
            [tests.events.score-test]
            [tests.events.collection-test]
            [tests.events-test]
            :reload))


(enable-console-print!)

(defn run-all-tests
  []
  (run-tests
   'tests.core
   'tests.events-test
   'tests.views-test
   'tests.db-test
   'tests.api.api-test
   'tests.events.score-test
   'tests.events.collection-test
   'tests.db-spec-test))


(defn rat [] (run-all-tests))
