;; gorilla-repl.fileformat = 1

;; **
;;; # Renjin Example: Calling pure-JVM R from Clojure
;;; 
;;; This tutorial is part of the [clojure data science course](https://clojure-data-science-course.github.io/). See more examples [here](https://github.com/clojure-data-science-course/examples).
;;; 
;;; It is a Gorilla REPL notebook. Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; In this worksheet, we show how to use Renjin from Clojure. [Renjin](http://www.renjin.org/) is a port of [R](https://www.r-project.org/) to Java. It runs on pure JVM. It supports many of R's package, but not all of them. It brings some important improvements over the usual R implementation: better memory manamgement, JIT compilation for fast loops, etc.
;;; 
;;; This is only one of several ways to bring R's functionality to Clojure. We will discuss more ways soon.
;;; 
;;; Most of the examples in this worksheet will soon be generalized in a library we are working on.
;;; 
;;; 
;;; 
;;; ~~~~
;;; 
;;; First, let us define our namespace.
;; **

;; @@
(ns examples.Renjin
  (:require [alembic.still :refer [distill]]
            [clojure.java.browse :refer [browse-url]]
            [clojure.java.shell :refer [sh]]
            [clojure.pprint :refer [pprint print-table]]))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Now, let us make Renjin available. We bring it just as any other Java or Clojure library, which is available as a Maven artifact.
;;; 
;; **

;; @@
(distill '[org.renjin/renjin-script-engine "0.9.2718"]
         :repositories '[["bedatadriven public repo"
    			                {:url "https://nexus.bedatadriven.com/content/groups/public/"}]])
;; @@
;; ->
;;; Loaded dependencies:
;;; [[commons-logging &quot;1.1.1&quot;]
;;;  [regexp &quot;1.3&quot;]
;;;  [com.github.fommil/jniloader &quot;1.1&quot;]
;;;  [com.github.fommil.netlib/all &quot;1.1.2&quot; :extension &quot;pom&quot;]
;;;  [com.github.fommil.netlib/core &quot;1.1.2&quot;]
;;;  [com.github.fommil.netlib/native_ref-java &quot;1.1&quot;]
;;;  [com.github.fommil.netlib/native_system-java &quot;1.1&quot;]
;;;  [com.github.fommil.netlib/netlib-native_ref-linux-armhf
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_ref-linux-i686
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_ref-linux-x86_64
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_ref-osx-x86_64
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_ref-win-i686
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_ref-win-x86_64
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_system-linux-armhf
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_system-linux-i686
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_system-linux-x86_64
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_system-osx-x86_64
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_system-win-i686
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.github.fommil.netlib/netlib-native_system-win-x86_64
;;;   &quot;1.1&quot;
;;;   :classifier
;;;   &quot;natives&quot;]
;;;  [com.sun.codemodel/codemodel &quot;2.6&quot;]
;;;  [net.sourceforge.f2j/arpack_combined_all &quot;0.1&quot;]
;;;  [org.apache.commons/commons-compress &quot;1.18&quot;]
;;;  [org.apache.commons/commons-math &quot;2.2&quot;]
;;;  [org.apache.commons/commons-vfs2 &quot;2.0&quot;]
;;;  [org.apache.maven.scm/maven-scm-api &quot;1.4&quot;]
;;;  [org.apache.maven.scm/maven-scm-provider-svn-commons &quot;1.4&quot;]
;;;  [org.apache.maven.scm/maven-scm-provider-svnexe &quot;1.4&quot;]
;;;  [org.codehaus.plexus/plexus-utils &quot;1.5.6&quot;]
;;;  [org.jfree/jfreesvg &quot;3.3&quot;]
;;;  [org.renjin/compiler &quot;0.9.2718&quot;]
;;;  [org.renjin/datasets &quot;0.9.2718&quot;]
;;;  [org.renjin/gcc-runtime &quot;0.9.2718&quot;]
;;;  [org.renjin/grDevices &quot;0.9.2718&quot;]
;;;  [org.renjin/graphics &quot;0.9.2718&quot;]
;;;  [org.renjin/methods &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-appl &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-asm &quot;5.0.4b&quot;]
;;;  [org.renjin/renjin-blas &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-core &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-gnur-runtime &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-guava &quot;17.0b&quot;]
;;;  [org.renjin/renjin-lapack &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-math-common &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-nmath &quot;0.9.2718&quot;]
;;;  [org.renjin/renjin-script-engine &quot;0.9.2718&quot;]
;;;  [org.renjin/stats &quot;0.9.2718&quot;]
;;;  [org.renjin/tools &quot;0.9.2718&quot;]
;;;  [org.renjin/utils &quot;0.9.2718&quot;]
;;;  [org.tukaani/xz &quot;1.8&quot;]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; We import some of its Java classes for convenience:
;; **

;; @@
(import    javax.script.ScriptEngineManager
           org.renjin.script.RenjinScriptEngine
           org.renjin.invoke.reflection.converters.Converters
           org.renjin.eval.Context
           org.renjin.parser.RParser
           (org.renjin.sexp SEXP Symbol Environment Environment$Builder
                            ListVector DoubleArrayVector Vector Null))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-class'>org.renjin.sexp.Null</span>","value":"org.renjin.sexp.Null"}
;; <=

;; **
;;; We also bring the [ggplot2](https://ggplot2.tidyverse.org/) package. Like Renjin itself, R packages for Renjin are available as Maven artifacts.
;; **

;; @@
(distill '[org.renjin.cran/ggplot2 "3.0.0-b29"]
         :repositories '[["bedatadriven public repo"
    			                {:url "https://nexus.bedatadriven.com/content/groups/public/"}]])
;; @@
;; ->
;;; WARN: org.renjin/gcc-runtime version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/renjin-blas version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/renjin-gnur-runtime version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/grDevices version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/graphics version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/renjin-core version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/renjin-nmath version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/tools version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/stats version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/renjin-appl version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/utils version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/datasets version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/renjin-math-common version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: commons-logging version 1.2 requested, but 1.1.1 already on classpath.
;;; WARN: org.apache.commons/commons-compress version 1.4.1 requested, but 1.18 already on classpath.
;;; WARN: org.renjin/renjin-lapack version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; WARN: org.renjin/methods version 0.9.2691 requested, but 0.9.2718 already on classpath.
;;; Loaded dependencies:
;;; [[regexp &quot;1.3&quot;]
;;;  [com.github.fommil.netlib/core &quot;1.1.2&quot;]
;;;  [com.ibm.icu/icu4j &quot;59.1&quot;]
;;;  [com.sun.codemodel/codemodel &quot;2.6&quot;]
;;;  [net.sourceforge.f2j/arpack_combined_all &quot;0.1&quot;]
;;;  [org.apache.commons/commons-math &quot;2.2&quot;]
;;;  [org.apache.commons/commons-vfs2 &quot;2.0&quot;]
;;;  [org.apache.maven.scm/maven-scm-api &quot;1.4&quot;]
;;;  [org.apache.maven.scm/maven-scm-provider-svn-commons &quot;1.4&quot;]
;;;  [org.apache.maven.scm/maven-scm-provider-svnexe &quot;1.4&quot;]
;;;  [org.apache.pdfbox/fontbox &quot;2.0.9&quot;]
;;;  [org.apache.pdfbox/pdfbox &quot;2.0.9&quot;]
;;;  [org.codehaus.plexus/plexus-utils &quot;1.5.6&quot;]
;;;  [org.jfree/jfreesvg &quot;3.3&quot;]
;;;  [org.renjin/grid &quot;0.9.2691&quot;]
;;;  [org.renjin/libstdcxx &quot;4.7.4-b34&quot;]
;;;  [org.renjin/parallel &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-asm &quot;5.0.4b&quot;]
;;;  [org.renjin/renjin-guava &quot;17.0b&quot;]
;;;  [org.renjin/splines &quot;0.9.2691&quot;]
;;;  [org.renjin/stats4 &quot;0.9.2691&quot;]
;;;  [org.renjin/tcltk &quot;0.9.2691&quot;]
;;;  [org.renjin.cran/MASS &quot;7.3-50-b17&quot;]
;;;  [org.renjin.cran/Matrix &quot;1.2-14-b21&quot;]
;;;  [org.renjin.cran/R6 &quot;2.2.2-b60&quot;]
;;;  [org.renjin.cran/RColorBrewer &quot;1.1-2-b338&quot;]
;;;  [org.renjin.cran/Rcpp &quot;0.12.13-renjin-15&quot;]
;;;  [org.renjin.cran/assertthat &quot;0.2.0-b58&quot;]
;;;  [org.renjin.cran/cli &quot;1.0.0-b43&quot;]
;;;  [org.renjin.cran/colorspace &quot;1.3-2-b57&quot;]
;;;  [org.renjin.cran/crayon &quot;1.3.4-b32&quot;]
;;;  [org.renjin.cran/digest &quot;0.6.16-b5&quot;]
;;;  [org.renjin.cran/fansi &quot;0.3.0-b17&quot;]
;;;  [org.renjin.cran/ggplot2 &quot;3.0.0-b29&quot;]
;;;  [org.renjin.cran/glue &quot;1.3.0-b18&quot;]
;;;  [org.renjin.cran/gtable &quot;0.2.0-b114&quot;]
;;;  [org.renjin.cran/labeling &quot;0.3-b329&quot;]
;;;  [org.renjin.cran/lattice &quot;0.20-35-b86&quot;]
;;;  [org.renjin.cran/lazyeval &quot;0.2.1-b35&quot;]
;;;  [org.renjin.cran/magrittr &quot;1.5-b352&quot;]
;;;  [org.renjin.cran/mgcv &quot;1.8-24-b16&quot;]
;;;  [org.renjin.cran/munsell &quot;0.5.0-b16&quot;]
;;;  [org.renjin.cran/nlme &quot;3.1-137-b23&quot;]
;;;  [org.renjin.cran/pillar &quot;1.3.0-b16&quot;]
;;;  [org.renjin.cran/plyr &quot;1.8.4-b98&quot;]
;;;  [org.renjin.cran/reshape2 &quot;1.4.3-b32&quot;]
;;;  [org.renjin.cran/rlang &quot;0.2.2-b25&quot;]
;;;  [org.renjin.cran/scales &quot;1.0.0-b16&quot;]
;;;  [org.renjin.cran/stringi &quot;1.1.6-renjin-b31&quot;]
;;;  [org.renjin.cran/stringr &quot;1.3.1-b18&quot;]
;;;  [org.renjin.cran/tibble &quot;1.4.2-b23&quot;]
;;;  [org.renjin.cran/utf8 &quot;1.1.4-b16&quot;]
;;;  [org.renjin.cran/viridisLite &quot;0.3.0-b23&quot;]
;;;  [org.renjin.cran/withr &quot;2.1.2-b22&quot;]
;;;  [org.tukaani/xz &quot;1.0&quot;]]
;;; Dependencies not loaded due to conflict with previous jars :
;;; [[commons-logging &quot;1.2&quot;]
;;;  [org.apache.commons/commons-compress &quot;1.4.1&quot;]
;;;  [org.renjin/datasets &quot;0.9.2691&quot;]
;;;  [org.renjin/gcc-runtime &quot;0.9.2691&quot;]
;;;  [org.renjin/grDevices &quot;0.9.2691&quot;]
;;;  [org.renjin/graphics &quot;0.9.2691&quot;]
;;;  [org.renjin/methods &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-appl &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-blas &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-core &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-gnur-runtime &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-lapack &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-math-common &quot;0.9.2691&quot;]
;;;  [org.renjin/renjin-nmath &quot;0.9.2691&quot;]
;;;  [org.renjin/stats &quot;0.9.2691&quot;]
;;;  [org.renjin/tools &quot;0.9.2691&quot;]
;;;  [org.renjin/utils &quot;0.9.2691&quot;]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; We also bring the [gg4clj](https://github.com/JonyEpsilon/gg4clj) library, that offers to write R code using Clojure data strucutes.
;; **

;; @@
(distill '[gg4clj "0.1.0"])
(require '[gg4clj.core :as gg4clj :refer [to-r]])
;; @@
;; ->
;;; WARN: gorilla-renderable version 1.0.0 requested, but 2.0.0 already on classpath.
;;; WARN: org.clojure/clojure version 1.6.0 requested, but 1.9.0 already on classpath.
;;; Loaded dependencies:
;;; [[gg4clj &quot;0.1.0&quot;]]
;;; Dependencies not loaded due to conflict with previous jars :
;;; [[gorilla-renderable &quot;1.0.0&quot;] [org.clojure/clojure &quot;1.6.0&quot;]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Now let us write some auxiliary functions for using Renjin.
;; **

;; @@
(def manager (ScriptEngineManager.))

(def engine ^RenjinScriptEngine (.getEngineByName ^ScriptEngineManager manager "Renjin"))

(defn reval [^String source]
  (.eval ^RenjinScriptEngine engine source))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/reval</span>","value":"#'examples.Renjin/reval"}
;; <=

;; **
;;; With `reval` we can call R code and get the return value as a Java object.
;;; 
;;; For example:
;; **

;; @@
(reval "1+2")
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#object[org.renjin.sexp.DoubleArrayVector 0x67a4bcfd &quot;3.0&quot;]</span>","value":"#object[org.renjin.sexp.DoubleArrayVector 0x67a4bcfd \"3.0\"]"}
;; <=

;; **
;;; Doing `1+2`, we got an R vector with one element: the number 3. 
;;; 
;;; In R, a number is always a vector with one element (and similarly for other basic data types).
;;; 
;;; Let us translate it to a Clojure vector:
;; **

;; @@
(vec (reval "1+2"))
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-double'>3.0</span>","value":"3.0"}],"value":"[3.0]"}
;; <=

;; **
;;; See [the documentation](http://docs.renjin.org/en/latest/library/moving-data-between-java-and-r-code.html#a-java-developer-s-guide-to-r-objects) of R object types in Renjin.
;;; 
;;; All R objects implement the `SEXP` (S-Expression) interface.
;;; 
;;; The NULL object is similar to Clojure's nil.
;;; 
;;; R objects usually have 'attributes', similar to the (less frequently used) metadata of Clojure objects. Here is how we can extract them.
;; **

;; @@
(defn NULL->nil [obj]
  (if (= Null/INSTANCE obj)
    nil
    obj))

(defn ->attr [^SEXP sexp attr-name]
  (-> sexp
 	  (.getAttribute (Symbol/get attr-name))
   	  NULL->nil))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/-&gt;attr</span>","value":"#'examples.Renjin/->attr"}
;; <=

;; **
;;; So, for example, a named list has the attribute `"names"`:
;; **

;; @@
(-> "list(a=1, b=2)"
  	reval
    (->attr "names")
    vec)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;a&quot;</span>","value":"\"a\""},{"type":"html","content":"<span class='clj-string'>&quot;b&quot;</span>","value":"\"b\""}],"value":"[\"a\" \"b\"]"}
;; <=

;; **
;;; This is important enough to have a dedicated function:
;; **

;; @@
(defn ->names [^SEXP sexp]
  (some->> (->attr sexp "names")
           (mapv keyword)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/-&gt;names</span>","value":"#'examples.Renjin/->names"}
;; <=

;; @@
(-> "list(a=1, b=2)"
  	reval
    ->names)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:a</span>","value":":a"},{"type":"html","content":"<span class='clj-keyword'>:b</span>","value":":b"}],"value":"[:a :b]"}
;; <=

;; **
;;; Another important common attribute is `"class"`, that tells about the type (or types) of an object.
;; **

;; @@
(defn ->class [^SEXP sexp]
  (some->> (->attr sexp "class")
           (mapv keyword)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/-&gt;class</span>","value":"#'examples.Renjin/->class"}
;; <=

;; @@
(-> "data.frame(x=1:3, y=sin(1:3))"
  	reval
  	->class)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:data.frame</span>","value":":data.frame"}],"value":"[:data.frame]"}
;; <=

;; **
;;; Here we created a data frame - one of R's most important data types.
;;; 
;;; It had two columns: `x` has the numbers 1 to 3, and `y` has their trigonometric sine values.
;;; 
;;; We can see the column names using the `"names"` attribute.
;; **

;; @@
(-> "data.frame(x=1:3, y=sin(1:3))"
  	reval
  	->names)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"}],"value":"[:x :y]"}
;; <=

;; **
;;; A data frame is implemented in Renjin as a java object of class `ListVector`.
;; **

;; @@
(-> "data.frame(x=1:3, y=sin(1:3))"
  	reval)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#object[org.renjin.sexp.ListVector 0x7f8aa1cf &quot;list(x = c(1L, 2L, 3L), y = c(0.8414709848079, 0.90929742682568, 0.14112000805987))&quot;]</span>","value":"#object[org.renjin.sexp.ListVector 0x7f8aa1cf \"list(x = c(1L, 2L, 3L), y = c(0.8414709848079, 0.90929742682568, 0.14112000805987))\"]"}
;; <=

;; **
;;; We can conveniently translate that object to a clojure map, whose keys are the column names, and values are the columns.
;; **

;; @@
(defn listvector->map [^ListVector lv]
  (zipmap (->names lv)
          (mapv vec lv)))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/listvector-&gt;map</span>","value":"#'examples.Renjin/listvector->map"}
;; <=

;; @@
(-> "data.frame(x=1:3, y=sin(1:3))"
  	reval
    listvector->map
    pprint)
;; @@
;; ->
;;; {:x [1 2 3],
;;;  :y [0.8414709848078965 0.9092974268256817 0.1411200080598672]}
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; We can also translate it to a sequence of maps, each representing a row (with the map keys as column names, and values as the corresponding values).
;; **

;; @@
(defn listvector->maps [^ListVector lv]
  (->> lv
       (map vec)
       (apply map
              (fn [& args]
                (zipmap (->names lv) args)))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/listvector-&gt;maps</span>","value":"#'examples.Renjin/listvector->maps"}
;; <=

;; @@
(-> "data.frame(x=1:3, y=sin(1:3))"
  	reval
    listvector->maps)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-unkown'>1</span>","value":"1"}],"value":"[:x 1]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-double'>0.8414709848078965</span>","value":"0.8414709848078965"}],"value":"[:y 0.8414709848078965]"}],"value":"{:x 1, :y 0.8414709848078965}"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-unkown'>2</span>","value":"2"}],"value":"[:x 2]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-double'>0.9092974268256817</span>","value":"0.9092974268256817"}],"value":"[:y 0.9092974268256817]"}],"value":"{:x 2, :y 0.9092974268256817}"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-unkown'>3</span>","value":"3"}],"value":"[:x 3]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-double'>0.1411200080598672</span>","value":"0.1411200080598672"}],"value":"[:y 0.1411200080598672]"}],"value":"{:x 3, :y 0.1411200080598672}"}],"value":"({:x 1, :y 0.8414709848078965} {:x 2, :y 0.9092974268256817} {:x 3, :y 0.1411200080598672})"}
;; <=

;; @@
(-> "data.frame(x=1:3, y=sin(1:3))"
  	reval
    listvector->maps
    print-table)
;; @@
;; ->
;;; 
;;; | :x |                 :y |
;;; |----+--------------------|
;;; |  1 | 0.8414709848078965 |
;;; |  2 | 0.9092974268256817 |
;;; |  3 | 0.1411200080598672 |
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Using gg4clj's function `to-r`, we can write R code as a Clojure data strucure.
;; **

;; @@
(-> [:data.frame {:x [:seq 1 3]
                  :y [:sin [:seq 1 3]]}]
    to-r)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;data.frame(x = seq(1, 3), y = sin(seq(1, 3)))&quot;</span>","value":"\"data.frame(x = seq(1, 3), y = sin(seq(1, 3)))\""}
;; <=

;; @@
(-> [:data.frame {:x [:seq 1 3]
                  :y [:sin [:seq 1 3]]}]
    to-r
    reval
  	listvector->maps)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-unkown'>1</span>","value":"1"}],"value":"[:x 1]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-double'>0.8414709848078965</span>","value":"0.8414709848078965"}],"value":"[:y 0.8414709848078965]"}],"value":"{:x 1, :y 0.8414709848078965}"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-unkown'>2</span>","value":"2"}],"value":"[:x 2]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-double'>0.9092974268256817</span>","value":"0.9092974268256817"}],"value":"[:y 0.9092974268256817]"}],"value":"{:x 2, :y 0.9092974268256817}"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-unkown'>3</span>","value":"3"}],"value":"[:x 3]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-double'>0.1411200080598672</span>","value":"0.1411200080598672"}],"value":"[:y 0.1411200080598672]"}],"value":"{:x 3, :y 0.1411200080598672}"}],"value":"({:x 1, :y 0.8414709848078965} {:x 2, :y 0.9092974268256817} {:x 3, :y 0.1411200080598672})"}
;; <=

;; **
;;; Let us load the [ggplot2](https://ggplot2.tidyverse.org/) library for plotting.
;; **

;; @@
(-> [:library :ggplot2]
  	to-r
  	reval)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#object[org.renjin.sexp.StringArrayVector 0x49e97d63 &quot;c(ggplot2, stats, graphics, grDevices, utils...8 elements total)&quot;]</span>","value":"#object[org.renjin.sexp.StringArrayVector 0x49e97d63 \"c(ggplot2, stats, graphics, grDevices, utils...8 elements total)\"]"}
;; <=

;; **
;;; We write a convenience function, to save a given plot to a temporary SVG file, and return the file name.
;; **

;; @@
(reval "plot_to_svg_file <- function(p) {filename<-paste0(tempfile(),'.svg'); svg(filename); print(p); dev.off(); filename}")
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#object[org.renjin.sexp.Closure 0x3c0c632 &quot;function(p=&lt;missing_arg&gt;)&quot;]</span>","value":"#object[org.renjin.sexp.Closure 0x3c0c632 \"function(p=<missing_arg>)\"]"}
;; <=

;; **
;;; Let us take 99 normally random numbers and use ggplot2's `qplot` function to plot them as histogram, saving the result to a fule.
;; **

;; @@
(-> [:plot_to_svg_file [:qplot [:rnorm 99]]]
  	to-r
  	reval
  	vec)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;/tmp/fileb12ab62feab973da.svg&quot;</span>","value":"\"/tmp/fileb12ab62feab973da.svg\""}],"value":"[\"/tmp/fileb12ab62feab973da.svg\"]"}
;; <=

;; **
;;; Now we will show the resulting file in Gorilla REPL. To do this, we need to read the file and turn the resulting object into something that is renderable by Gorilla.
;;; 
;; **

;; @@
(defn svg->gorilla [svg]
  (reify gorilla-renderable.core/Renderable
    (render [self]
      {:type    :html
       :content svg
       :value   (pr-str self)})))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/svg-&gt;gorilla</span>","value":"#'examples.Renjin/svg->gorilla"}
;; <=

;; @@
(-> [:plot_to_svg_file [:qplot [:rnorm 99]]]
  	to-r
  	reval
  	first
  	slurp
  	svg->gorilla)
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\"?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:jfreesvg=\"http://www.jfree.org/jfreesvg/svg\" width=\"504\" height=\"504\" text-rendering=\"auto\" shape-rendering=\"auto\">\n<defs><clipPath id=\"_99668375716556clip-0\"><path d=\"M 0 0 L 505 0 L 505 505 L 0 505 L 0 0 Z \"/></clipPath>\n<clipPath id=\"_99668375716556clip-1\"><path d=\"M 46 8 L 497 8 L 497 461 L 46 461 L 46 8 Z \"/></clipPath>\n</defs>\n<rect x=\"0\" y=\"0\" width=\"504\" height=\"504\" style=\"fill: rgb(255,255,255); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" /><rect x=\"0\" y=\"0\" width=\"504\" height=\"504\" style=\"fill: rgb(255,255,255); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" /><rect x=\"0\" y=\"0\" width=\"505\" height=\"505\" style=\"fill: rgb(255,255,255); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"/><line x1=\"0\" y1=\"0\" x2=\"503\" y2=\"0\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"/><line x1=\"504\" y1=\"0\" x2=\"504\" y2=\"503\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"/><line x1=\"504\" y1=\"504\" x2=\"1\" y2=\"504\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"/><line x1=\"0\" y1=\"504\" x2=\"0\" y2=\"1\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"/><rect x=\"46\" y=\"8\" width=\"451\" height=\"453\" style=\"fill: rgb(235,235,235); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 382 L 496 382\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 268 L 496 268\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 154 L 496 154\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 40 L 496 40\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 56 460 L 56 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 150 460 L 150 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 245 460 L 245 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 339 460 L 339 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 433 460 L 433 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 439 L 496 439\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 325 L 496 325\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 211 L 496 211\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 46 97 L 496 97\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 103 460 L 103 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 197 460 L 197 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 292 460 L 292 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 386 460 L 386 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"><path d=\"M 480 460 L 480 8\"/></g><rect x=\"67\" y=\"394\" width=\"14\" height=\"46\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"80\" y=\"439\" width=\"15\" height=\"1\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"94\" y=\"439\" width=\"15\" height=\"1\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"108\" y=\"439\" width=\"14\" height=\"1\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"121\" y=\"439\" width=\"15\" height=\"1\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"135\" y=\"394\" width=\"15\" height=\"46\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"149\" y=\"348\" width=\"14\" height=\"92\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"162\" y=\"348\" width=\"15\" height=\"92\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"176\" y=\"211\" width=\"14\" height=\"229\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"189\" y=\"348\" width=\"15\" height=\"92\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"203\" y=\"302\" width=\"15\" height=\"138\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"217\" y=\"394\" width=\"14\" height=\"46\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"230\" y=\"302\" width=\"15\" height=\"138\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"244\" y=\"165\" width=\"15\" height=\"275\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"258\" y=\"119\" width=\"14\" height=\"321\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"271\" y=\"119\" width=\"15\" height=\"321\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"285\" y=\"119\" width=\"15\" height=\"321\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"299\" y=\"165\" width=\"14\" height=\"275\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"312\" y=\"211\" width=\"15\" height=\"229\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"326\" y=\"119\" width=\"15\" height=\"321\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"340\" y=\"165\" width=\"14\" height=\"275\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"353\" y=\"165\" width=\"15\" height=\"275\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"367\" y=\"257\" width=\"14\" height=\"183\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"380\" y=\"28\" width=\"15\" height=\"412\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"394\" y=\"257\" width=\"15\" height=\"183\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"408\" y=\"302\" width=\"14\" height=\"138\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"421\" y=\"394\" width=\"15\" height=\"46\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"435\" y=\"439\" width=\"15\" height=\"1\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"449\" y=\"439\" width=\"14\" height=\"1\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><rect x=\"462\" y=\"394\" width=\"15\" height=\"46\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-1)\"/><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"33\" y=\"444\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">0</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"25\" y=\"330\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">2.5</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"33\" y=\"215\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">5</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"25\" y=\"101\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">7.5</text></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 42 439 L 46 439\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 42 325 L 46 325\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 42 211 L 46 211\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 42 97 L 46 97\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 103 464 L 103 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 197 464 L 197 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 292 464 L 292 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 386 464 L 386 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99668375716556clip-0)\"><path d=\"M 480 464 L 480 460\"/></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"99\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">-2</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"193\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">-1</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"289\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">0</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"383\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">1</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"477\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99668375716556clip-0)\">2</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"243\" y=\"493\" style=\"fill: rgb(0,0,0); fill-opacity: 1.0; font-family: sans-serif; font-size: 11px;\" clip-path=\"url(#_99668375716556clip-0)\">rnorm(99)</text></g></svg>\n","value":"#object[examples.Renjin$svg$reify__14854 0x41c5fa37 \"examples.Renjin$svg$reify__14854@41c5fa37\"]"}
;; <=

;; **
;;; One nice thing that R has is a collection of example [datasets](https://stat.ethz.ch/R-manual/R-devel/library/datasets/html/00Index.html). One of them is the famous [Iris dataset](https://en.wikipedia.org/wiki/Iris_flower_data_set). We can use its data in clojure. Let us print some rows.
;; **

;; @@
(->> "iris"
     reval
     listvector->maps
  	 (take 4)
  	 print-table)
;; @@
;; ->
;;; 
;;; | :Sepal.Length | :Sepal.Width | :Petal.Length | :Petal.Width | :Species |
;;; |---------------+--------------+---------------+--------------+----------|
;;; |           5.1 |          3.5 |           1.4 |          0.2 |        1 |
;;; |           4.9 |          3.0 |           1.4 |          0.2 |        1 |
;;; |           4.7 |          3.2 |           1.3 |          0.2 |        1 |
;;; |           4.6 |          3.1 |           1.5 |          0.2 |        1 |
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Now we write some auxiliary functions to help us translate Clojure data structures to Renjin data strucures, and run R code while binding some names to the corresponding values.
;; **

;; @@
(defn top-level-context []
  (-> engine bean :topLevelContext))

(defn ->symbol [k]
  (Symbol/get (name k)))

(defn ->renjin [obj]
  (let [prepared-obj (cond
                       ;; sequential
                       (sequential? obj)
                       (let [elem1 (first obj)]
                         (cond
                           ;; numbers
                           (number? elem1)
                           (->> obj
                                (map #(double (or % Double/NaN)))
                                double-array)
                           ;; strings or keywords
                           (or (string? elem1)
                               (instance? clojure.lang.Named elem1))
                           (->> obj
                                (map #(some-> % name))
                                (into-array String))
                           ;; else
                           :else obj))
                       ;; else
                       :else obj)]
    (Converters/fromJava prepared-obj)))


(defn- get-child-env-with-bindings [bindings-map]
  (let [child-env (->> ^Context (top-level-context)
                       (.getEnvironment)
                       (Environment/createChildEnvironment))]
    (doseq [[k v] bindings-map]
      (.setVariable ^Environment$Builder child-env
                    ^Symbol (->symbol k)
                    ^SEXP (->renjin v)))
    (.build child-env)))

(defn read-renjin-string [^String r-code]
  (RParser/parseSource r-code))

(defn eval-with-bindings [r-codes bindings-map]
  (let [env     (get-child-env-with-bindings bindings-map)
        context (.beginEvalContext ^Context (top-level-context)
                                   env)]
    (->> r-codes
         (map (fn [s]
                (.evaluate context
                           (read-renjin-string (str s "\n")))))
         last)))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.Renjin/eval-with-bindings</span>","value":"#'examples.Renjin/eval-with-bindings"}
;; <=

;; **
;;; Let us try it. We can apply R's median function to some data we create in Clojure.
;; **

;; @@
(->> (eval-with-bindings ["mean(x)"]
                         {:x (range 100)})
  	 vec)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-double'>49.5</span>","value":"49.5"}],"value":"[49.5]"}
;; <=

;; **
;;; This makes sense: the median of 0,1,...,100 is 49.5.
;;; 
;;; Thanks to R's (and Renjin's) notion of environment, bindings of names to values could be comfortably done in local scope. That is, the binding of "x" to the Clojure data was done in the local scope of the computation above. If we try to access it without the bindings now, it will fail:
;; **

;; @@
(reval "x")
;; @@

;; **
;;; Now let us plot some clojure data in this way.
;; **

;; @@
(-> (eval-with-bindings [(to-r [:plot_to_svg_file [:qplot :x]])]
                        {:x (repeatedly 10000 rand)})
  	first
  	slurp
  	svg->gorilla)
;; @@
;; =>
;;; {"type":"html","content":"<?xml version=\"1.0\"?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:jfreesvg=\"http://www.jfree.org/jfreesvg/svg\" width=\"504\" height=\"504\" text-rendering=\"auto\" shape-rendering=\"auto\">\n<defs><clipPath id=\"_99789061462487clip-0\"><path d=\"M 0 0 L 505 0 L 505 505 L 0 505 L 0 0 Z \"/></clipPath>\n<clipPath id=\"_99789061462487clip-1\"><path d=\"M 49 8 L 497 8 L 497 461 L 49 461 L 49 8 Z \"/></clipPath>\n</defs>\n<rect x=\"0\" y=\"0\" width=\"504\" height=\"504\" style=\"fill: rgb(255,255,255); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" /><rect x=\"0\" y=\"0\" width=\"504\" height=\"504\" style=\"fill: rgb(255,255,255); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" /><rect x=\"0\" y=\"0\" width=\"505\" height=\"505\" style=\"fill: rgb(255,255,255); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"/><line x1=\"0\" y1=\"0\" x2=\"503\" y2=\"0\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"/><line x1=\"504\" y1=\"0\" x2=\"504\" y2=\"503\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"/><line x1=\"504\" y1=\"504\" x2=\"1\" y2=\"504\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"/><line x1=\"0\" y1=\"504\" x2=\"0\" y2=\"1\" style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"/><rect x=\"49\" y=\"8\" width=\"448\" height=\"453\" style=\"fill: rgb(235,235,235); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 387 L 496 387\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 282 L 496 282\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 176 L 496 176\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 71 L 496 71\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 125 460 L 125 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 224 460 L 224 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 322 460 L 322 8\"/></g><g style=\"stroke-width: 0.7113189101219177;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 420 460 L 420 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 439 L 496 439\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 334 L 496 334\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 229 L 496 229\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 124 L 496 124\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 49 19 L 496 19\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 76 460 L 76 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 175 460 L 175 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 273 460 L 273 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 371 460 L 371 8\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(255,255,255);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"><path d=\"M 469 460 L 469 8\"/></g><rect x=\"70\" y=\"266\" width=\"14\" height=\"174\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"83\" y=\"58\" width=\"15\" height=\"382\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"97\" y=\"79\" width=\"14\" height=\"361\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"110\" y=\"71\" width=\"15\" height=\"369\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"124\" y=\"77\" width=\"14\" height=\"363\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"137\" y=\"28\" width=\"15\" height=\"412\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"151\" y=\"73\" width=\"14\" height=\"367\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"164\" y=\"86\" width=\"15\" height=\"354\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"178\" y=\"107\" width=\"15\" height=\"333\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"192\" y=\"98\" width=\"14\" height=\"342\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"205\" y=\"66\" width=\"15\" height=\"374\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"219\" y=\"69\" width=\"14\" height=\"371\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"232\" y=\"65\" width=\"15\" height=\"375\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"246\" y=\"41\" width=\"14\" height=\"399\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"259\" y=\"48\" width=\"15\" height=\"392\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"273\" y=\"57\" width=\"14\" height=\"383\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"286\" y=\"101\" width=\"15\" height=\"339\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"300\" y=\"92\" width=\"14\" height=\"348\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"313\" y=\"74\" width=\"15\" height=\"366\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"327\" y=\"62\" width=\"15\" height=\"378\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"341\" y=\"136\" width=\"14\" height=\"304\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"354\" y=\"75\" width=\"15\" height=\"365\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"368\" y=\"92\" width=\"14\" height=\"348\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"381\" y=\"104\" width=\"15\" height=\"336\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"395\" y=\"90\" width=\"14\" height=\"350\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"408\" y=\"68\" width=\"15\" height=\"372\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"422\" y=\"63\" width=\"14\" height=\"377\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"435\" y=\"77\" width=\"15\" height=\"363\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"449\" y=\"103\" width=\"15\" height=\"337\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><rect x=\"463\" y=\"238\" width=\"14\" height=\"202\" style=\"fill: rgb(89,89,89); fill-opacity: 1.0\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-1)\"/><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"36\" y=\"444\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">0</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"25\" y=\"339\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">100</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"25\" y=\"233\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">200</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"25\" y=\"128\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">300</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"25\" y=\"23\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">400</text></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 45 439 L 49 439\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 45 334 L 49 334\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 45 229 L 49 229\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 45 124 L 49 124\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 45 19 L 49 19\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 76 464 L 76 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 175 464 L 175 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 273 464 L 273 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 371 464 L 371 460\"/></g><g style=\"stroke-width: 1.4226378202438354;stroke: rgb(51,51,51);stroke-opacity: 1.0;stroke-linecap: square;; fill: none\" transform=\"matrix(1,0,0,1,0,0)\" clip-path=\"url(#_99789061462487clip-0)\"><path d=\"M 469 464 L 469 460\"/></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"73\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">0</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"165\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">0.25</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"266\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">0.5</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"361\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">0.75</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"466\" y=\"476\" style=\"fill: rgb(77,77,77); fill-opacity: 1.0; font-family: sans-serif; font-size: 9px;\" clip-path=\"url(#_99789061462487clip-0)\">1</text></g><g transform=\"matrix(1,0,0,1,0,0)\"><text x=\"269\" y=\"493\" style=\"fill: rgb(0,0,0); fill-opacity: 1.0; font-family: sans-serif; font-size: 11px;\" clip-path=\"url(#_99789061462487clip-0)\">x</text></g></svg>\n","value":"#object[examples.Renjin$svg$reify__14854 0x58793e00 \"examples.Renjin$svg$reify__14854@58793e00\"]"}
;; <=

;; **
;;; These were some examples of using Renjin from Clojure. More work is necessary to make it into a robust library.
;;; 
;;; For now, it seems to be a rather fruitful direction for bringing a huge collection of pure-JVM statistical functions to Clojure.
;;; 
;;; Comments will be welcome! -- you may use the [Issues](https://github.com/clojure-data-science-course/examples/issues) of this github repo, or [contact me](https://twitter.com/daslu_) elsewhere.
;; **

;; @@

;; @@
