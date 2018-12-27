;; gorilla-repl.fileformat = 1

;; **
;;; # Basic Machine Learning Example
;;; 
;;; This tutorial is part of the [clojure data science course](https://clojure-data-science-course.github.io/). See more examples [here](https://github.com/clojure-data-science-course/examples).
;;; 
;;; In this worksheet we give some basic example of a machine learning workflow in Clojure, using the Java library [Smile](https://haifengl.github.io/smile/). It is based on the [second meeting](https://clojure-data-science-course.github.io/posts-output/2018-12-11-towards-the-second-meeting/) of the course, with some changes for the sake of presentation.
;;; 
;;; **Remarks.** 
;;; - In this tutorial, we try to keep things rather basic, and knowinglny neglect some important aspects such as reproducibility, extensibility, reusability, etc.
;;; - We do not not use [type hints](https://clojure.org/reference/java_interop#typehints) here, since we wanted to keep the code easier to read. If you intend to work with Java a lot, you may wish to learn about them - they are important for performance when calling java methods many times (which was not the case here).
;;; - To keep things as simple and easy as possible, we dealt only with numerical variables here. 
;;; - Stay tuned for more complicated examples soon!
;;; 
;;; 🌺 Special thanks to Michael Feinstein, Ella Bekerman-Vizel, Alon Vizel and Alik Peltinovich, who helped to make these notes a little better. 🌺
;;; 
;;; ~~~~
;;; 
;;; First, let us make sure that some relevant libraries are available:  [Statistiker](https://github.com/clojurewerkz/statistiker) for statistics, [Smile](https://haifengl.github.io/smile/) for machine learning, [Rhizome](https://github.com/ztellman/rhizome) for visualizing trees, and [core.matrix](https://github.com/mikera/core.matrix), [Fluokitten](https://fluokitten.uncomplicate.org/) and [Specter](https://github.com/nathanmarz/specter) for all kinds of data processing.
;;; 
;;; 
;; **

;; @@
(require '[alembic.still])

(->>
  '[[clojurewerkz/statistiker "0.1.0-SNAPSHOT"]
	[com.github.haifengl/smile-core "1.5.2"]
	[rhizome "0.2.9"]
	[uncomplicate/fluokitten "0.9.0"]
	[com.rpl/specter "1.1.2"]
	[net.mikera/core.matrix "0.62.0"]]
  (map alembic.still/distill)
  doall)

;; @@

;; **
;;; Now let us define our namespace, together with some requirements.
;; **

;; @@
(ns examples.machine-learning
   (:require [gorilla-plot.core :as plot]
             [gorilla-repl.table :as table]
             [gorilla-renderable.core :as render]
            [clojure.pprint :refer [pprint print-table]]
            [clojure.repl :refer [pst]]
            [clojurewerkz.statistiker.statistics :as stat]
            [clojure.repl :refer [doc pst]]
            [uncomplicate.fluokitten.core :refer [fmap]]
            [com.rpl.specter :refer [transform ALL MAP-VALS]]
            [clojure.core.matrix :as mat]
            [rhizome.viz])
  (:import (smile.regression RegressionTree RandomForest)))


;; @@

;; **
;;; Let us define some dummy data.
;;; 
;;; First, `x` will be a matrix of 1000 rows and 4 columns of random numbers.
;; **

;; @@
(def x 
  (vec (repeatedly 1000 (fn [] (vec (repeatedly 4 rand))))))
;; @@

;; **
;;; Let us look at some rows.
;; **

;; @@
(->> x
  (take 5)
  (cons [:v1 :v2 :v3 :v4])
  (table/table-view))
;; @@

;; **
;;; Now, `y` will be generated from `x`, together with some more randomness.
;;; 
;;; Note the notation `[[v1 v2 v3 v4]]` in the function arguments below - we use [destructuring](https://clojure.org/guides/destructuring).
;;; 
;;; Given a row of `x`, whose elements are `v1 v2 v3 v4`, we generate an element of `y` in the following way: 
;;; - if `v1<0.5`, 
;;;   - then if `v3<0.2`, 
;;;     - then it will be `3`,
;;;     - otherwise, it will be `v1`,
;;;   - otherwise, if `v4<0.6`,
;;;     - then it will be `1`,
;;;     - otherwise, it will be `v1+v2`,
;;; - to that, we add some more randomness in any case.
;; **

;; @@
(def y
  (->> x
       (map (fn [[v1 v2 v3 v4]]
              (+ (if (< v1 0.5)
                   (if (< v3 0.2)
                     2
                     v1)
                   (if (< v4 0.6) 
                     1
                     (+ v1 v2)))
                 (rand))))))
;; @@

;; **
;;; Let us look at some rows and their corresponding y values.
;; **

;; @@
(->> (map conj x y)
  (take 5)
  (cons [:v1 :v2 :v3 :v4 :y])
  (table/table-view))
;; @@

;; **
;;; To perform machine learning, we randomly split our data into training and test sets.
;; **

;; @@
(def train-or-test
  (repeatedly (fn []
                (if (< (rand) 0.5)
                  :train
                  :test))))

(defn split [data]
  (->> data
       (map vector train-or-test)
       (group-by first)
       (transform [MAP-VALS ALL] second)))

(def x-datasets (split x))
(def y-datasets (split y))
;; @@

;; **
;;; So, `x-datasets` holds the training and test sets in a map, and `y-datasets`, too, respectively.
;;; Let us see some part of these sets.
;; **

;; @@
(->> x-datasets
     (fmap (fn [data]
             (->> data
                  (take 3)
                  table/table-view))))
;; @@

;; @@
(->> y-datasets
     (fmap (fn [data]
             (->> data
                  (take 3)
                  (map vector)
                  table/table-view))))
;; @@

;; **
;;; To use our data with the Smile Java library, we need to translate clojure vectors into java arrays.
;;; 
;;; Let us transform our datasets accordingly.
;; **

;; @@
(def x-java-datasets
  (->> x-datasets
       (fmap (fn [data]
               (->> data
                    (map double-array)
                    into-array)))))

(def y-java-datasets
  (->> y-datasets
       (fmap (fn [data]
               (->> data
                    double-array)))))

;; @@

;; **
;;; So, each of the `x` datasets, in its java form, is a 2d double array, and each of the `y` datasets is a 1d double array.
;; **

;; @@
(->> x-java-datasets
     (fmap type))
;; @@

;; @@
(->> y-java-datasets
     (fmap type))
;; @@

;; **
;;; Now, we may try to implement a regression tree on the training set, and test it on the test set.
;;; 
;;; We use [this](https://haifengl.github.io/smile/api/java/smile/regression/RegressionTree.html#RegressionTree-double:A:A-double:A-int-int-) constructor, that takes `x`, `y` and two more parameters that affect the tree construction: `maxNodes` and `nodeSize`.
;;; 
;; **

;; @@
(let [max-nodes 20
      node-size 10
      tree (RegressionTree. (:train x-java-datasets)
                            (:train y-java-datasets)
                            max-nodes
                            node-size)
      prediction (.predict tree (:test x-java-datasets))
      reality 	 (:test y-java-datasets)
      error 	 (map - prediction reality)]
  (table/table-view
   {:scatter-plot 			(plot/list-plot (map vector prediction reality)
                                  :x-title "prediction"
                                  :y-title "reality")
    :error-histogram 		(plot/histogram error
                                     :x-title "error")
    :median-absolute-error 	(-> error mat/abs stat/median)
    :mean-square-error 		(-> error mat/square stat/mean)}))
;; @@

;; **
;;; Now, let us generalize this, to compare several models.
;;; 
;;; First, let us write a general function to train a model. The model may be one of the following:
;;; - a regression tree (constructed with [this](https://haifengl.github.io/smile/api/java/smile/regression/RegressionTree.html#RegressionTree-double:A:A-double:A-int-int-) constructor)
;;; - a regression random forest (constructed with [this](https://haifengl.github.io/smile/api/java/smile/regression/RandomForest.html#RandomForest-double:A:A-double:A-int-int-int-int-) constructor)
;;; 
;;; We define a general `train` function, that takes the relevant parameters as a map, using [destructuring](https://clojure.org/guides/destructuring), and returns the trained model.
;;; 
;; **

;; @@
(defn train [{:keys [;; model type -- either :tree or :forest
                     model-type 
                     ;; parameters relevant for both tree and forest:
                     max-nodes
                     node-size 
                     ;; parameters relevant only for forest
                     n-trees
                     mtry]}]
  (case model-type
    :tree 	(RegressionTree. (:train x-java-datasets)
                	         (:train y-java-datasets)
            	             max-nodes
                    	     node-size)
    :forest (RandomForest.	 (:train x-java-datasets)
                	         (:train y-java-datasets)
                             n-trees
                             max-nodes
                    	     node-size
                             mtry)))
;; @@

;; **
;;; Let us try it:
;; **

;; @@
(train {:model-type :forest
        :max-nodes 20
        :node-size 10
        :n-trees 1000
        :mtry 3})
;; @@

;; **
;;; So, we asked it to create a trained regression random forest, and it seems to have created one.
;;; 
;;; Now, let us create a function for training a model and testing its performance - both over the training and the test sets.
;; **

;; @@
(defn train-and-test [training-parameters]
  (let [model (train training-parameters)]
    {:training-parameters training-parameters
     :model model
     :results 
      (for [dataset-type [:train :test]]
        (let [prediction (.predict model (dataset-type x-java-datasets))
     		  reality 	 (dataset-type y-java-datasets)
      		  error 	 (map - prediction reality)
              color		 (case dataset-type
                          :train "magenta"
                          :test	 "blue")]
         {:dataset-type				dataset-type
          :scatter-plot 			(plot/list-plot (map vector prediction reality)
                                		  	:x-title "prediction"
                                  			:y-title "reality"
                                            :plot-range [[0 5] [0 5]]
                                            :color color)
  		  :error-histogram 			(plot/histogram error
                            	         	:x-title "error"
                                            :plot-range [[-3 3] :all]
                                            :bins 100
                                            :color color)
    	  :median-absolute-error 	(-> error mat/abs stat/median)
    	  :mean-square-error 		(-> error mat/square stat/mean)}))}))
;; @@

;; **
;;; Let us try it:
;; **

;; @@
(defn view-model-results [model-results]
  (-> model-results
      (dissoc :model)
      (update :training-parameters (comp table/table-view vector))
      (update :results table/table-view)))

;; @@

;; @@
(-> {:model-type :tree
     :max-nodes 50
     :node-size 2}
    train-and-test
    view-model-results)
;; @@

;; **
;;; We see that with these parameters, there is some overfitting - errors tend to be smaller on the training set, compared to the test set.
;;; 
;;; Now let us compare several models. We sort them by the least mean square error over the test set.
;; **

;; @@
(def models-compared
  (->> 
    (for [model-type 	[:tree :forest]
       	   max-nodes 	[4 16]
      	   node-size	[2 4 8]
           n-trees		(case model-type
                    	  :tree [nil]
                      	  :forest [100 1000])
           mtry			(case model-type
                    	  :tree [nil]
                      	  :forest [2 3])]
		(train-and-test
    	 {:model-type 	model-type 	
      	  :max-nodes 	max-nodes 	
      	  :node-size 	node-size		
      	  :n-trees 		n-trees		
      	  :mtry 		mtry}))
    (sort-by (fn [train-and-test-results]
            			(->> train-and-test-results
                   			:results
                   			(filter #(-> % :dataset-type (= :test)))
                   			first
                  			:mean-square-error)))))


;; @@

;; **
;;; Let us view the 4 best models - in other words - those who have the least mean square error over the test set.
;; **

;; @@
(->> models-compared
	 (take 4)
     (map view-model-results))
;; @@

;; **
;;; The best random forest (that is, the one who got the smallest mean sqare error over the test set) was:
;; **

;; @@
(def best-forest-results
  (->> models-compared
     (filter (fn [model-results]
               (-> model-results
                   :training-parameters
                   :model-type
                   (= :forest))))
     first))

(view-model-results best-forest-results)
;; @@

;; **
;;; And the best tree was:
;; **

;; @@
(def best-tree-results
  (->> models-compared
     (filter (fn [model-results]
               (-> model-results
                   :training-parameters
                   :model-type
                   (= :tree))))
     first))

(view-model-results best-tree-results)
;; @@

;; **
;;; Generally speaking, random forests are more robust agains overfitting, compared to single trees. They achieve that by averaging many variations of trees, that are each trained in slightly different conditions.
;;; 
;; **

;; **
;;; 
;;; Let us look at that one best tree. To view it, we will use the java method `.dot` to transform it to the common [DOT language](https://www.graphviz.org/doc/info/lang.html). If you have [Graphviz](https://www.graphviz.org/) installed, you will be able to use the clojure library [Rhizome](https://github.com/ztellman/rhizome/blob/master/src/rhizome/viz.clj) translate it to the common [SVG](https://en.wikipedia.org/wiki/Scalable_Vector_Graphics) format.
;;; 
;;; 
;; **

;; **
;;; SVG can be viewed in the browser. All that remains is to tell Gorilla REPL's [renderer](http://gorilla-repl.org/renderer.html) about it, and we can see it.
;; **

;; @@
(defn svg->gorilla [svg]
  (reify render/Renderable
    (render [self]
      {:type    :html
       :content svg
       :value   (pr-str self)})))

(defn view-tree [tree]
  (-> tree
      (.dot)
      rhizome.viz/dot->svg
  	  svg->gorilla))
;; @@

;; **
;;; Now let us try it with our tree.
;; **

;; @@
(->> best-tree-results
     :model
     view-tree)

;; 🌳
;; @@

;; **
;;; We can see how the original way of creating the data is reflected in the resulting tree.
;;; 
;;; Now, to see the diversity in a random forest, let us take pick some trees of the best random forest and view them.
;; **

;; @@
(->> best-forest-results
     :model
     (.getTrees)
     (take 3)
     (map view-tree))

;; 🌳 🌳 🌳
;; @@

;; **
;;; Now, let us show the population of all models we have trained, over a scatter plot. One axis shows the mean square error on the training set, and the other - on the test set.
;; **

;; @@
(->> models-compared
     (group-by (fn [model-results]
                 (-> model-results 
                   	 :training-parameters 
                     :model-type)))
     (map (fn [[model-type models-results]]
             (plot/list-plot
                  (->> models-results
                       (map (fn [model-results]
                         (->> model-results
                              :results
                              (map :mean-square-error)))))
                  :x-title :training-mean-square-error
                  :y-title :test-mean-square-error
                  :color (case model-type
                           :tree "green"
                           :forest "brown")
              	  :opacity 0.2)))
      (apply plot/compose))
     
;; @@

;; **
;;; Trees are green, forests are brown.
;;; 
;;; The interesting models are those with small training and test errors. When the test error is relatively large compared to the training error, this means overfitting.
;; **

;; **
;;; 
;; **
