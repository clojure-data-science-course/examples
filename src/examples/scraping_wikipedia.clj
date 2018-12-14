;; gorilla-repl.fileformat = 1

;; **
;;; # Example: Scraping Wikipedia
;;; 
;;; In this example we show some typical data processing workflow in clojure.
;;; 
;;; It is a Gorilla REPL notebook. Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; We put our code in a clojure namespace:
;;; 
;; **

;; @@
(ns examples.scraping-wikipedia)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; 
;; **

;; **
;;; First, Let us load some libraries that we may need.
;;; The function `distill` of the `alembic` package helps us make sure the libraries are available: it brings them from the central web repositories, and lets this running program know that they are there.
;;; The function `require` makes specific namespaces available in the current namespace.
;;; 
;;;  
;; **

;; @@
(require '[gorilla-plot.core :refer [list-plot compose]]
         '[clojure.pprint :refer [pprint print-table]]
         '[alembic.still :refer [distill]]
         '[clojure.string :as string])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; 
;; **

;; @@
(distill '[com.rpl/specter "1.1.2"])

(require '[com.rpl.specter :refer [transform ALL walker select]])
;; @@
;; ->
;;; Loaded dependencies:
;;; [[riddley &quot;0.1.12&quot;] [com.rpl/specter &quot;1.1.2&quot;]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(distill '[clj-http "3.9.1"])
(require '[clj-http.client :as client])
;; @@
;; ->
;;; WARN: commons-io version 2.6 requested, but 2.4 already on classpath.
;;; WARN: commons-codec version 1.11 requested, but 1.10 already on classpath.
;;; Loaded dependencies:
;;; [[clj-http &quot;3.9.1&quot;]
;;;  [clj-tuple &quot;0.2.2&quot;]
;;;  [commons-logging &quot;1.2&quot;]
;;;  [potemkin &quot;0.4.5&quot; :exclusions [[org.clojure/clojure]]]
;;;  [riddley &quot;0.1.12&quot;]
;;;  [slingshot &quot;0.12.2&quot; :exclusions [[org.clojure/clojure]]]
;;;  [org.apache.httpcomponents/httpasyncclient
;;;   &quot;4.1.3&quot;
;;;   :exclusions
;;;   [[org.clojure/clojure]]]
;;;  [org.apache.httpcomponents/httpclient
;;;   &quot;4.5.5&quot;
;;;   :exclusions
;;;   [[org.clojure/clojure]]]
;;;  [org.apache.httpcomponents/httpclient-cache
;;;   &quot;4.5.5&quot;
;;;   :exclusions
;;;   [[org.clojure/clojure]]]
;;;  [org.apache.httpcomponents/httpcore
;;;   &quot;4.4.9&quot;
;;;   :exclusions
;;;   [[org.clojure/clojure]]]
;;;  [org.apache.httpcomponents/httpcore-nio &quot;4.4.6&quot;]
;;;  [org.apache.httpcomponents/httpmime
;;;   &quot;4.5.5&quot;
;;;   :exclusions
;;;   [[org.clojure/clojure]]]]
;;; Dependencies not loaded due to conflict with previous jars :
;;; [[commons-codec &quot;1.11&quot; :exclusions [[org.clojure/clojure]]]
;;;  [commons-io &quot;2.6&quot; :exclusions [[org.clojure/clojure]]]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(distill '[clj-tagsoup "0.3.0"])
(require '[pl.danieljanus.tagsoup :as tagsoup])
;; @@
;; ->
;;; WARN: org.clojure/clojure version 1.10.0-RC5 requested, but 1.8.0 already on classpath.
;;; Loaded dependencies:
;;; [[clj-tagsoup &quot;0.3.0&quot;]
;;;  [net.java.dev.stax-utils/stax-utils &quot;20040917&quot;]
;;;  [org.clojars.nathell/tagsoup &quot;1.2.1&quot;]
;;;  [org.clojure/core.specs.alpha &quot;0.2.44&quot;]
;;;  [org.clojure/data.xml &quot;0.0.3&quot;]
;;;  [org.clojure/spec.alpha &quot;0.2.176&quot;]]
;;; Dependencies not loaded due to conflict with previous jars :
;;; [[org.clojure/clojure &quot;1.10.0-RC5&quot;]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(distill '[cheshire "5.8.1"])
(require '[cheshire.core :as cheshire])
;; @@
;; ->
;;; WARN: org.clojure/clojure version 1.5.1 requested, but 1.8.0 already on classpath.
;;; Loaded dependencies:
;;; [[cheshire &quot;5.8.1&quot;]
;;;  [tigris &quot;0.1.1&quot;]
;;;  [com.fasterxml.jackson.core/jackson-core &quot;2.9.6&quot;]
;;;  [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor &quot;2.9.6&quot;]
;;;  [com.fasterxml.jackson.dataformat/jackson-dataformat-smile &quot;2.9.6&quot;]]
;;; Dependencies not loaded due to conflict with previous jars :
;;; [[org.clojure/clojure &quot;1.5.1&quot;]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(distill '[gg4clj "0.1.0"])
(require '[gg4clj.core :as gg4clj])
;; @@
;; ->
;;; WARN: gorilla-renderable version 1.0.0 requested, but 2.0.0 already on classpath.
;;; WARN: org.clojure/clojure version 1.6.0 requested, but 1.8.0 already on classpath.
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
;;; Let us limit the pring length and depth of huge data structures:
;; **

;; @@
(set! *print-length* 10)
(set! *print-level* 5)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>5</span>","value":"5"}
;; <=

;; **
;;; Let us look at [this](https://en.wikipedia.org/wiki/List_of_countries_by_distribution_of_wealth) wikipedia page about the distribution of wealth in different countries.
;;; 
;;; We will try to extract the table out ot if.
;;; 
;;; First, let us read the page and keep it in a clojure var:
;;; 
;;; 
;; **

;; @@
(def response
    (client/get "https://en.wikipedia.org/wiki/List_of_countries_by_distribution_of_wealth"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.scraping-wikipedia/response</span>","value":"#'examples.scraping-wikipedia/response"}
;; <=

;; **
;;; What do we have in this response we got from the web?
;; **

;; @@
(type response)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-class'>clojure.lang.PersistentHashMap</span>","value":"clojure.lang.PersistentHashMap"}
;; <=

;; **
;;; It is a clojure map. What are its keys?
;; **

;; @@
(keys response)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>(:cached :request-time :repeatable? :protocol-version :streaming? :http-client :chunked? :cookies :reason-phrase :headers ...)</span>","value":"(:cached :request-time :repeatable? :protocol-version :streaming? :http-client :chunked? :cookies :reason-phrase :headers ...)"}
;; <=

;; **
;;; It turns out that the `body` key contains the actual html body of the page we asked for.
;;; 
;;; Let us print a substring of this html:
;; **

;; @@
(-> response
    :body
    (subs 0 1000))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;&lt;!DOCTYPE html&gt;\\n&lt;html class=\\&quot;client-nojs\\&quot; lang=\\&quot;en\\&quot; dir=\\&quot;ltr\\&quot;&gt;\\n&lt;head&gt;\\n&lt;meta charset=\\&quot;UTF-8\\&quot;/&gt;\\n&lt;title&gt;List of countries by distribution of wealth - Wikipedia&lt;/title&gt;\\n&lt;script&gt;document.documentElement.className = document.documentElement.className.replace( /(^|\\\\s)client-nojs(\\\\s|$)/, \\&quot;$1client-js$2\\&quot; );&lt;/script&gt;\\n&lt;script&gt;(window.RLQ=window.RLQ||[]).push(function(){mw.config.set({\\&quot;wgCanonicalNamespace\\&quot;:\\&quot;\\&quot;,\\&quot;wgCanonicalSpecialPageName\\&quot;:false,\\&quot;wgNamespaceNumber\\&quot;:0,\\&quot;wgPageName\\&quot;:\\&quot;List_of_countries_by_distribution_of_wealth\\&quot;,\\&quot;wgTitle\\&quot;:\\&quot;List of countries by distribution of wealth\\&quot;,\\&quot;wgCurRevisionId\\&quot;:859462648,\\&quot;wgRevisionId\\&quot;:859462648,\\&quot;wgArticleId\\&quot;:31789942,\\&quot;wgIsArticle\\&quot;:true,\\&quot;wgIsRedirect\\&quot;:false,\\&quot;wgAction\\&quot;:\\&quot;view\\&quot;,\\&quot;wgUserName\\&quot;:null,\\&quot;wgUserGroups\\&quot;:[\\&quot;*\\&quot;],\\&quot;wgCategories\\&quot;:[\\&quot;Wikipedia articles in need of updating from August 2017\\&quot;,\\&quot;All Wikipedia articles in need of updating\\&quot;,\\&quot;Distribution of wealth\\&quot;,\\&quot;Lists of countries by economic indicator\\&quot;,\\&quot;Lists by population\\&quot;,\\&quot;Economic inequality\\&quot;,\\&quot;Gross domestic produc&quot;</span>","value":"\"<!DOCTYPE html>\\n<html class=\\\"client-nojs\\\" lang=\\\"en\\\" dir=\\\"ltr\\\">\\n<head>\\n<meta charset=\\\"UTF-8\\\"/>\\n<title>List of countries by distribution of wealth - Wikipedia</title>\\n<script>document.documentElement.className = document.documentElement.className.replace( /(^|\\\\s)client-nojs(\\\\s|$)/, \\\"$1client-js$2\\\" );</script>\\n<script>(window.RLQ=window.RLQ||[]).push(function(){mw.config.set({\\\"wgCanonicalNamespace\\\":\\\"\\\",\\\"wgCanonicalSpecialPageName\\\":false,\\\"wgNamespaceNumber\\\":0,\\\"wgPageName\\\":\\\"List_of_countries_by_distribution_of_wealth\\\",\\\"wgTitle\\\":\\\"List of countries by distribution of wealth\\\",\\\"wgCurRevisionId\\\":859462648,\\\"wgRevisionId\\\":859462648,\\\"wgArticleId\\\":31789942,\\\"wgIsArticle\\\":true,\\\"wgIsRedirect\\\":false,\\\"wgAction\\\":\\\"view\\\",\\\"wgUserName\\\":null,\\\"wgUserGroups\\\":[\\\"*\\\"],\\\"wgCategories\\\":[\\\"Wikipedia articles in need of updating from August 2017\\\",\\\"All Wikipedia articles in need of updating\\\",\\\"Distribution of wealth\\\",\\\"Lists of countries by economic indicator\\\",\\\"Lists by population\\\",\\\"Economic inequality\\\",\\\"Gross domestic produc\""}
;; <=

;; **
;;; Let us parse the html string, translating it to a clojure data structure.
;;; 
;;; Note that in the printing below, some parts of the datastructur are not shown, as we limited the printing of huge data structures.
;; **

;; @@
(->> response
     :body
      tagsoup/parse-string
      pprint)
;; @@
;; ->
;;; [:html
;;;  {:class &quot;client-nojs&quot;, :dir &quot;ltr&quot;, :lang &quot;en&quot;}
;;;  [:head
;;;   {}
;;;   [:meta {:charset &quot;UTF-8&quot;}]
;;;   [:title {} &quot;List of countries by distribution of wealth - Wikipedia&quot;]
;;;   [:script
;;;    {}
;;;    &quot;document.documentElement.className = document.documentElement.className.replace( /(^|\\s)client-nojs(\\s|$)/, \&quot;$1client-js$2\&quot; );&quot;]
;;;   [:script
;;;    {}
;;;    &quot;(window.RLQ=window.RLQ||[]).push(function(){mw.config.set({\&quot;wgCanonicalNamespace\&quot;:\&quot;\&quot;,\&quot;wgCanonicalSpecialPageName\&quot;:false,\&quot;wgNamespaceNumber\&quot;:0,\&quot;wgPageName\&quot;:\&quot;List_of_countries_by_distribution_of_wealth\&quot;,\&quot;wgTitle\&quot;:\&quot;List of countries by distribution of wealth\&quot;,\&quot;wgCurRevisionId\&quot;:859462648,\&quot;wgRevisionId\&quot;:859462648,\&quot;wgArticleId\&quot;:31789942,\&quot;wgIsArticle\&quot;:true,\&quot;wgIsRedirect\&quot;:false,\&quot;wgAction\&quot;:\&quot;view\&quot;,\&quot;wgUserName\&quot;:null,\&quot;wgUserGroups\&quot;:[\&quot;*\&quot;],\&quot;wgCategories\&quot;:[\&quot;Wikipedia articles in need of updating from August 2017\&quot;,\&quot;All Wikipedia articles in need of updating\&quot;,\&quot;Distribution of wealth\&quot;,\&quot;Lists of countries by economic indicator\&quot;,\&quot;Lists by population\&quot;,\&quot;Economic inequality\&quot;,\&quot;Gross domestic product\&quot;,\&quot;Income distribution\&quot;,\&quot;Wealth by country\&quot;,\&quot;Global inequality\&quot;,\&quot;International rankings\&quot;],\&quot;wgBreakFrames\&quot;:false,\&quot;wgPageContentLanguage\&quot;:\&quot;en\&quot;,\&quot;wgPageContentModel\&quot;:\&quot;wikitext\&quot;,\&quot;wgSeparatorTransformTable\&quot;:[\&quot;\&quot;,\&quot;\&quot;],\&quot;wgDigitTransformTable\&quot;:[\&quot;\&quot;,\&quot;\&quot;],\&quot;wgDefaultDateFormat\&quot;:\&quot;dmy\&quot;,\&quot;wgMonthNames\&quot;:[\&quot;\&quot;,\&quot;January\&quot;,\&quot;February\&quot;,\&quot;March\&quot;,\&quot;April\&quot;,\&quot;May\&quot;,\&quot;June\&quot;,\&quot;July\&quot;,\&quot;August\&quot;,\&quot;September\&quot;,\&quot;October\&quot;,\&quot;November\&quot;,\&quot;December\&quot;],\&quot;wgMonthNamesShort\&quot;:[\&quot;\&quot;,\&quot;Jan\&quot;,\&quot;Feb\&quot;,\&quot;Mar\&quot;,\&quot;Apr\&quot;,\&quot;May\&quot;,\&quot;Jun\&quot;,\&quot;Jul\&quot;,\&quot;Aug\&quot;,\&quot;Sep\&quot;,\&quot;Oct\&quot;,\&quot;Nov\&quot;,\&quot;Dec\&quot;],\&quot;wgRelevantPageName\&quot;:\&quot;List_of_countries_by_distribution_of_wealth\&quot;,\&quot;wgRelevantArticleId\&quot;:31789942,\&quot;wgRequestId\&quot;:\&quot;XBO7JgpAIC4AACDpjBcAAAAW\&quot;,\&quot;wgCSPNonce\&quot;:false,\&quot;wgIsProbablyEditable\&quot;:true,\&quot;wgRelevantPageIsProbablyEditable\&quot;:true,\&quot;wgRestrictionEdit\&quot;:[],\&quot;wgRestrictionMove\&quot;:[],\&quot;wgFlaggedRevsParams\&quot;:{\&quot;tags\&quot;:{}},\&quot;wgStableRevisionId\&quot;:null,\&quot;wgCategoryTreePageCategoryOptions\&quot;:\&quot;{\\\&quot;mode\\\&quot;:0,\\\&quot;hideprefix\\\&quot;:20,\\\&quot;showcount\\\&quot;:true,\\\&quot;namespaces\\\&quot;:false}\&quot;,\&quot;wgWikiEditorEnabledModules\&quot;:[],\&quot;wgBetaFeaturesFeatures\&quot;:[],\&quot;wgMediaViewerOnClick\&quot;:true,\&quot;wgMediaViewerEnabledByDefault\&quot;:true,\&quot;wgPopupsShouldSendModuleToUser\&quot;:true,\&quot;wgPopupsConflictsWithNavPopupGadget\&quot;:false,\&quot;wgVisualEditor\&quot;:{\&quot;pageLanguageCode\&quot;:\&quot;en\&quot;,\&quot;pageLanguageDir\&quot;:\&quot;ltr\&quot;,\&quot;pageVariantFallbacks\&quot;:\&quot;en\&quot;,\&quot;usePageImages\&quot;:true,\&quot;usePageDescriptions\&quot;:true},\&quot;wgMFExpandAllSectionsUserOption\&quot;:true,\&quot;wgMFEnableFontChanger\&quot;:true,\&quot;wgMFDisplayWikibaseDescriptions\&quot;:{\&quot;search\&quot;:true,\&quot;nearby\&quot;:true,\&quot;watchlist\&quot;:true,\&quot;tagline\&quot;:false},\&quot;wgRelatedArticles\&quot;:null,\&quot;wgRelatedArticlesUseCirrusSearch\&quot;:true,\&quot;wgRelatedArticlesOnlyUseCirrusSearch\&quot;:false,\&quot;wgWMESchemaEditAttemptStepOversample\&quot;:false,\&quot;wgULSCurrentAutonym\&quot;:\&quot;English\&quot;,\&quot;wgNoticeProject\&quot;:\&quot;wikipedia\&quot;,\&quot;wgCentralNoticeCookiesToDelete\&quot;:[],\&quot;wgCentralNoticeCategoriesUsingLegacy\&quot;:[\&quot;Fundraising\&quot;,\&quot;fundraising\&quot;],\&quot;wgWikibaseItemId\&quot;:\&quot;Q830082\&quot;,\&quot;wgScoreNoteLanguages\&quot;:{\&quot;arabic\&quot;:\&quot;العربية\&quot;,\&quot;catalan\&quot;:\&quot;català\&quot;,\&quot;deutsch\&quot;:\&quot;Deutsch\&quot;,\&quot;english\&quot;:\&quot;English\&quot;,\&quot;espanol\&quot;:\&quot;español\&quot;,\&quot;italiano\&quot;:\&quot;italiano\&quot;,\&quot;nederlands\&quot;:\&quot;Nederlands\&quot;,\&quot;norsk\&quot;:\&quot;norsk\&quot;,\&quot;portugues\&quot;:\&quot;português\&quot;,\&quot;suomi\&quot;:\&quot;suomi\&quot;,\&quot;svenska\&quot;:\&quot;svenska\&quot;,\&quot;vlaams\&quot;:\&quot;West-Vlams\&quot;},\&quot;wgScoreDefaultNoteLanguage\&quot;:\&quot;nederlands\&quot;,\&quot;wgCentralAuthMobileDomain\&quot;:false,\&quot;wgCodeMirrorEnabled\&quot;:true,\&quot;wgVisualEditorToolbarScrollOffset\&quot;:0,\&quot;wgVisualEditorUnsupportedEditParams\&quot;:[\&quot;undo\&quot;,\&quot;undoafter\&quot;,\&quot;veswitched\&quot;],\&quot;wgEditSubmitButtonLabelPublish\&quot;:true});mw.loader.state({\&quot;ext.gadget.charinsert-styles\&quot;:\&quot;ready\&quot;,\&quot;ext.globalCssJs.user.styles\&quot;:\&quot;ready\&quot;,\&quot;ext.globalCssJs.site.styles\&quot;:\&quot;ready\&quot;,\&quot;site.styles\&quot;:\&quot;ready\&quot;,\&quot;noscript\&quot;:\&quot;ready\&quot;,\&quot;user.styles\&quot;:\&quot;ready\&quot;,\&quot;ext.globalCssJs.user\&quot;:\&quot;ready\&quot;,\&quot;ext.globalCssJs.site\&quot;:\&quot;ready\&quot;,\&quot;user\&quot;:\&quot;ready\&quot;,\&quot;user.options\&quot;:\&quot;ready\&quot;,\&quot;user.tokens\&quot;:\&quot;loading\&quot;,\&quot;ext.cite.styles\&quot;:\&quot;ready\&quot;,\&quot;mediawiki.legacy.shared\&quot;:\&quot;ready\&quot;,\&quot;mediawiki.legacy.commonPrint\&quot;:\&quot;ready\&quot;,\&quot;mediawiki.toc.styles\&quot;:\&quot;ready\&quot;,\&quot;wikibase.client.init\&quot;:\&quot;ready\&quot;,\&quot;ext.visualEditor.desktopArticleTarget.noscript\&quot;:\&quot;ready\&quot;,\&quot;ext.uls.interlanguage\&quot;:\&quot;ready\&quot;,\&quot;ext.wikimediaBadges\&quot;:\&quot;ready\&quot;,\&quot;ext.3d.styles\&quot;:\&quot;ready\&quot;,\&quot;mediawiki.skinning.interface\&quot;:\&quot;ready\&quot;,\&quot;skins.vector.styles\&quot;:\&quot;ready\&quot;});mw.loader.implement(\&quot;user.tokens@0tffind\&quot;,function($,jQuery,require,module){/*@nomin*/mw.user.tokens.set({\&quot;editToken\&quot;:\&quot;+\\\\\&quot;,\&quot;patrolToken\&quot;:\&quot;+\\\\\&quot;,\&quot;watchToken\&quot;:\&quot;+\\\\\&quot;,\&quot;csrfToken\&quot;:\&quot;+\\\\\&quot;});\n});RLPAGEMODULES=[\&quot;ext.cite.ux-enhancements\&quot;,\&quot;site\&quot;,\&quot;mediawiki.page.startup\&quot;,\&quot;mediawiki.page.ready\&quot;,\&quot;jquery.tablesorter\&quot;,\&quot;mediawiki.toc\&quot;,\&quot;mediawiki.searchSuggest\&quot;,\&quot;ext.gadget.teahouse\&quot;,\&quot;ext.gadget.ReferenceTooltips\&quot;,\&quot;ext.gadget.watchlist-notice\&quot;,\&quot;ext.gadget.DRN-wizard\&quot;,\&quot;ext.gadget.charinsert\&quot;,\&quot;ext.gadget.refToolbar\&quot;,\&quot;ext.gadget.extra-toolbar-buttons\&quot;,\&quot;ext.gadget.switcher\&quot;,\&quot;ext.centralauth.centralautologin\&quot;,\&quot;mmv.head\&quot;,\&quot;mmv.bootstrap.autostart\&quot;,\&quot;ext.popups\&quot;,\&quot;ext.visualEditor.desktopArticleTarget.init\&quot;,\&quot;ext.visualEditor.targetLoader\&quot;,\&quot;ext.eventLogging.subscriber\&quot;,\&quot;ext.wikimediaEvents\&quot;,\&quot;ext.navigationTiming\&quot;,\&quot;ext.uls.eventlogger\&quot;,\&quot;ext.uls.init\&quot;,\&quot;ext.uls.compactlinks\&quot;,\&quot;ext.uls.interface\&quot;,\&quot;ext.centralNotice.geoIP\&quot;,\&quot;ext.centralNotice.startUp\&quot;,\&quot;skins.vector.js\&quot;];mw.loader.load(RLPAGEMODULES);});&quot;]
;;;   [:link
;;;    {:rel &quot;stylesheet&quot;,
;;;     :href
;;;     &quot;/w/load.php?debug=false&amp;lang=en&amp;modules=ext.3d.styles%7Cext.cite.styles%7Cext.uls.interlanguage%7Cext.visualEditor.desktopArticleTarget.noscript%7Cext.wikimediaBadges%7Cmediawiki.legacy.commonPrint%2Cshared%7Cmediawiki.skinning.interface%7Cmediawiki.toc.styles%7Cskins.vector.styles%7Cwikibase.client.init&amp;only=styles&amp;skin=vector&quot;}]
;;;   [:script
;;;    {:async &quot;&quot;,
;;;     :src
;;;     &quot;/w/load.php?debug=false&amp;lang=en&amp;modules=startup&amp;only=scripts&amp;skin=vector&quot;}]
;;;   [:meta {:name &quot;ResourceLoaderDynamicStyles&quot;, :content &quot;&quot;}]
;;;   [:link
;;;    {:rel &quot;stylesheet&quot;,
;;;     :href
;;;     &quot;/w/load.php?debug=false&amp;lang=en&amp;modules=ext.gadget.charinsert-styles&amp;only=styles&amp;skin=vector&quot;}]
;;;   ...]
;;;  [:body
;;;   {:class
;;;    &quot;mediawiki ltr sitedir-ltr mw-hide-empty-elt ns-0 ns-subject mw-editable page-List_of_countries_by_distribution_of_wealth rootpage-List_of_countries_by_distribution_of_wealth skin-vector action-view&quot;}
;;;   [:div {:class &quot;noprint&quot;, :id &quot;mw-page-base&quot;}]
;;;   [:div {:class &quot;noprint&quot;, :id &quot;mw-head-base&quot;}]
;;;   [:div
;;;    {:class &quot;mw-body&quot;, :id &quot;content&quot;, :role &quot;main&quot;}
;;;    [:a {#, #}]
;;;    [:div {#, #}]
;;;    [:div {#}]
;;;    [:h1 {#, #, #} &quot;List of countries by distribution of wealth&quot;]
;;;    [:div
;;;     {#, #}
;;;     [:div # &quot;From Wikipedia, the free encyclopedia&quot;]
;;;     [:div #]
;;;     [:div #]
;;;     [:a # &quot;Jump to navigation&quot;]
;;;     [:a # &quot;Jump to search&quot;]
;;;     [:div # # #]
;;;     [:div # &quot;\n\t\t\t\t\t\tRetrieved from \&quot;&quot; # &quot;\&quot;\t\t\t\t\t&quot;]
;;;     [:div # # #]
;;;     ...]]
;;;   [:div
;;;    {:id &quot;mw-navigation&quot;}
;;;    [:h2 {} &quot;Navigation menu&quot;]
;;;    [:div {#} [:div # # #] [:div # # #] [:div # # # #]]
;;;    [:div
;;;     {#}
;;;     [:div # #]
;;;     [:div # # #]
;;;     [:div # # #]
;;;     [:div # # #]
;;;     [:div # # #]
;;;     [:div # # #]]]
;;;   [:div
;;;    {:id &quot;footer&quot;, :role &quot;contentinfo&quot;}
;;;    [:ul
;;;     {#}
;;;     [:li
;;;      #
;;;      &quot; This page was last edited on 14 September 2018, at 06:06&quot;
;;;      #
;;;      &quot;.&quot;]
;;;     [:li
;;;      #
;;;      &quot;Text is available under the &quot;
;;;      #
;;;      #
;;;      &quot;;\nadditional terms may apply.  By using this site, you agree to the &quot;
;;;      #
;;;      &quot; and &quot;
;;;      #
;;;      &quot;. Wikipedia® is a registered trademark of the &quot;
;;;      ...]]
;;;    [:ul
;;;     {#}
;;;     [:li # #]
;;;     [:li # #]
;;;     [:li # #]
;;;     [:li # #]
;;;     [:li # #]
;;;     [:li # #]
;;;     [:li # #]]
;;;    [:ul {#, #} [:li # #] [:li # #]]
;;;    [:div {#}]]
;;;   [:script
;;;    {}
;;;    &quot;(window.RLQ=window.RLQ||[]).push(function(){mw.config.set({\&quot;wgPageParseReport\&quot;:{\&quot;limitreport\&quot;:{\&quot;cputime\&quot;:\&quot;0.948\&quot;,\&quot;walltime\&quot;:\&quot;1.595\&quot;,\&quot;ppvisitednodes\&quot;:{\&quot;value\&quot;:10715,\&quot;limit\&quot;:1000000},\&quot;ppgeneratednodes\&quot;:{\&quot;value\&quot;:0,\&quot;limit\&quot;:1500000},\&quot;postexpandincludesize\&quot;:{\&quot;value\&quot;:116869,\&quot;limit\&quot;:2097152},\&quot;templateargumentsize\&quot;:{\&quot;value\&quot;:17058,\&quot;limit\&quot;:2097152},\&quot;expansiondepth\&quot;:{\&quot;value\&quot;:11,\&quot;limit\&quot;:40},\&quot;expensivefunctioncount\&quot;:{\&quot;value\&quot;:1,\&quot;limit\&quot;:500},\&quot;unstrip-depth\&quot;:{\&quot;value\&quot;:1,\&quot;limit\&quot;:20},\&quot;unstrip-size\&quot;:{\&quot;value\&quot;:8113,\&quot;limit\&quot;:5000000},\&quot;entityaccesscount\&quot;:{\&quot;value\&quot;:0,\&quot;limit\&quot;:400},\&quot;timingprofile\&quot;:[\&quot;100.00% 1109.062      1 -total\&quot;,\&quot; 46.28%  513.306    150 Template:Flag\&quot;,\&quot;  8.93%   99.056      1 Template:Reflist\&quot;,\&quot;  7.82%   86.712      3 Template:Cite_web\&quot;,\&quot;  7.63%   84.585      1 Template:Update\&quot;,\&quot;  5.64%   62.496    150 Template:Flag/core\&quot;,\&quot;  5.24%   58.074      2 Template:Main_other\&quot;,\&quot;  4.89%   54.271      1 Template:Ambox\&quot;,\&quot;  2.71%   30.074      1 Template:Population_country_lists\&quot;,\&quot;  2.05%   22.731      1 Template:Navbox\&quot;]},\&quot;scribunto\&quot;:{\&quot;limitreport-timeusage\&quot;:{\&quot;value\&quot;:\&quot;0.099\&quot;,\&quot;limit\&quot;:\&quot;10.000\&quot;},\&quot;limitreport-memusage\&quot;:{\&quot;value\&quot;:2643729,\&quot;limit\&quot;:52428800}},\&quot;cachereport\&quot;:{\&quot;origin\&quot;:\&quot;mw1325\&quot;,\&quot;timestamp\&quot;:\&quot;20181214141607\&quot;,\&quot;ttl\&quot;:1900800,\&quot;transientcontent\&quot;:false}}});mw.config.set({\&quot;wgBackendResponseTime\&quot;:1716,\&quot;wgHostname\&quot;:\&quot;mw1325\&quot;});});&quot;]]]
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; This is a moment of joy: now we have the page as a clojure data structure, and we can use clojure's power and flexibility to query it.
;;; 
;;; Let us define an auxiliary function. Given a data structure `v` and a predicate function `pred`, it checks whether `v` is a sequential data structure, whose first element satisfies the condition defined by `pred`.
;; **

;; @@
(defn begins-with? [pred v]
  (and (sequential? v)
       (-> v first pred)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.scraping-wikipedia/begins-with?</span>","value":"#'examples.scraping-wikipedia/begins-with?"}
;; <=

;; **
;;; For example, does the sequence `[-9 5 1]` begin with a positive value?
;; **

;; @@
(begins-with? pos? [-9 5 1])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>false</span>","value":"false"}
;; <=

;; **
;;; One useful predicate function is a set. Yes, a set is a function which, when applied to an alement, returns the element itself if and only if the element is in the set (`nil` otherwise).
;;; 
;;; For example, with the set `#{:A :B}`:
;; **

;; @@
(#{:A :B} :C)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(#{:A :B} :A)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-keyword'>:A</span>","value":":A"}
;; <=

;; **
;;; Now, let us use it with our function `begins-with?`:
;; **

;; @@
(begins-with? #{:A :B} [:C :A]) 
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(begins-with? #{:A :B} [:A :D])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-keyword'>:A</span>","value":":A"}
;; <=

;; **
;;; Now let us use it to look for all vectors inside our data structure, that begins with `:tr`. These correspond to table-row elements in the original html. Let us take 3 such elements.
;;; 
;;; Note the use of the `select` and `walker` functions of the [Specter](https://github.com/nathanmarz/specter) library. Specter is really helpful with deeply nested data structure. You may like this [book](https://leanpub.com/specter/read) about it.
;; **

;; @@
(->> response
     :body
     tagsoup/parse-string
     (select (walker (fn [v]
                         (begins-with? #{:tr} v))))
     (take 3)
     pprint)
;; @@
;; ->
;;; ([:tr
;;;   {}
;;;   [:td
;;;    {:colspan &quot;1&quot;, :rowspan &quot;1&quot;, :class &quot;mbox-image&quot;}
;;;    [:div {#} [:img #]]]
;;;   [:td
;;;    {:colspan &quot;1&quot;, :rowspan &quot;1&quot;, :class &quot;mbox-text&quot;}
;;;    [:div
;;;     {#}
;;;     &quot;Parts of this article (those related to United States supposedly accounts for almost 40% of world&#x27;s wealth&quot;
;;;     [:sup # #]
;;;     [:sup # #]
;;;     [:sup # #]
;;;     &quot;) need to be &quot;
;;;     [:b # &quot;updated&quot;]
;;;     &quot;.&quot;
;;;     [:span
;;;      #
;;;      &quot; Please update this article to reflect recent events or newly available information.&quot;]
;;;     ...]]]
;;;  [:tr
;;;   {}
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;, :width &quot;250&quot;} &quot;Country\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Population (1000s)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Adults (1000s)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Share of world population (%)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Share of adult population (%)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Wealth per capita\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Wealth per adult\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Share of world wealth (%)\n&quot;]
;;;   ...]
;;;  [:tr
;;;   {}
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} [:br {#}]]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   ...])
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; How much elements do these vectors have inside them?
;; **

;; @@
(->> response
     :body
     tagsoup/parse-string
     (select (walker (fn [v]
                         (begins-with? #{:tr} v))))
     (map count))
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>3</span>","value":"3"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>4</span>","value":"4"},{"type":"html","content":"<span class='clj-unkown'>3</span>","value":"3"}],"value":"(4 13 13 13 13 13 13 13 13 13 ...)"}
;; <=

;; **
;;; Most of them have 13 elements. These are probabily the rows of the table we are looking for. Let us take 3 of these.
;; **

;; @@
(->> response
     :body
     tagsoup/parse-string
     (select (walker (fn [v]
                         (and (begins-with? #{:tr} v)
                              (-> v count (= 13))))))
     (take 3)
     pprint)
;; @@
;; ->
;;; ([:tr
;;;   {}
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;, :width &quot;250&quot;} &quot;Country\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Population (1000s)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Adults (1000s)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Share of world population (%)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Share of adult population (%)\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Wealth per capita\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Wealth per adult\n&quot;]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;Share of world wealth (%)\n&quot;]
;;;   ...]
;;;  [:tr
;;;   {}
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} [:br {#}]]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   [:th {:colspan &quot;1&quot;, :rowspan &quot;1&quot;}]
;;;   ...]
;;;  [:tr
;;;   {}
;;;   [:td
;;;    {:align &quot;left&quot;, :colspan &quot;1&quot;, :rowspan &quot;1&quot;}
;;;    [:i {} [:a # &quot;World&quot;]]]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;6085576&quot;]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;3697511&quot;]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;100.00&quot;]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;100.00&quot;]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;26416&quot;]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;43494&quot;]
;;;   [:td {:colspan &quot;1&quot;, :rowspan &quot;1&quot;} &quot;100.00&quot;]
;;;   ...])
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; We further process these rows with the `transform` and `ALL` functions of the Specter library.
;; **

;; @@
(->> response
     :body
     tagsoup/parse-string
     (select (walker (fn [v]
                         (and (begins-with? #{:tr} v)
                              (-> v count (> 4))))))
     (transform [ALL] #(drop 2 %))
     (transform [ALL ALL] last)
     (transform [ALL ALL]
                (fn [v] (cond (vector? v) (last v) 
                              (string? v) (string/replace v #"\n" ""))))
     (transform [ALL ALL]
                (fn [v] (cond (vector? v) (last v) 
                              (string? v) v)))
     (filter (fn [v] (-> v first)))
     butlast
     (take 4)
     pprint)
;; @@
;; ->
;;; ((&quot;Country&quot;
;;;   &quot;Population (1000s)&quot;
;;;   &quot;Adults (1000s)&quot;
;;;   &quot;Share of world population (%)&quot;
;;;   &quot;Share of adult population (%)&quot;
;;;   &quot;Wealth per capita&quot;
;;;   &quot;Wealth per adult&quot;
;;;   &quot;Share of world wealth (%)&quot;
;;;   &quot;GDP per capita&quot;
;;;   &quot;Share of world GDP (%)&quot;
;;;   ...)
;;;  (&quot;World&quot;
;;;   &quot;6085576&quot;
;;;   &quot;3697511&quot;
;;;   &quot;100.00&quot;
;;;   &quot;100.00&quot;
;;;   &quot;26416&quot;
;;;   &quot;43494&quot;
;;;   &quot;100.00&quot;
;;;   &quot;7675&quot;
;;;   &quot;100.00&quot;
;;;   ...)
;;;  (&quot;Albania&quot;
;;;   &quot;3062&quot;
;;;   &quot;1851&quot;
;;;   &quot;0.05&quot;
;;;   &quot;0.05&quot;
;;;   &quot;10574&quot;
;;;   &quot;17497&quot;
;;;   &quot;0.02&quot;
;;;   &quot;3658&quot;
;;;   &quot;0.02&quot;
;;;   ...)
;;;  (&quot;Algeria&quot;
;;;   &quot;30463&quot;
;;;   &quot;16353&quot;
;;;   &quot;0.50&quot;
;;;   &quot;0.44&quot;
;;;   &quot;7320&quot;
;;;   &quot;13635&quot;
;;;   &quot;0.14&quot;
;;;   &quot;6107&quot;
;;;   &quot;0.40&quot;
;;;   ...))
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; So we have got the rows of the table. Let us keep them in a clojure var.
;; **

;; @@
(def rows
    (->> response
     :body
     tagsoup/parse-string
     (select (walker (fn [v]
                         (and (begins-with? #{:tr} v)
                              (-> v count (> 4))))))
     (transform [ALL] #(drop 2 %))
     (transform [ALL ALL] last)
     (transform [ALL ALL]
                (fn [v] (cond (vector? v) (last v) 
                              (string? v) (string/replace v #"\n" ""))))
     (transform [ALL ALL]
                (fn [v] (cond (vector? v) (last v) 
                              (string? v) v)))
     (filter (fn [v] (-> v first)))
     butlast))


(->> rows
     (take 3)
     pprint)
;; @@
;; ->
;;; ((&quot;Country&quot;
;;;   &quot;Population (1000s)&quot;
;;;   &quot;Adults (1000s)&quot;
;;;   &quot;Share of world population (%)&quot;
;;;   &quot;Share of adult population (%)&quot;
;;;   &quot;Wealth per capita&quot;
;;;   &quot;Wealth per adult&quot;
;;;   &quot;Share of world wealth (%)&quot;
;;;   &quot;GDP per capita&quot;
;;;   &quot;Share of world GDP (%)&quot;
;;;   ...)
;;;  (&quot;World&quot;
;;;   &quot;6085576&quot;
;;;   &quot;3697511&quot;
;;;   &quot;100.00&quot;
;;;   &quot;100.00&quot;
;;;   &quot;26416&quot;
;;;   &quot;43494&quot;
;;;   &quot;100.00&quot;
;;;   &quot;7675&quot;
;;;   &quot;100.00&quot;
;;;   ...)
;;;  (&quot;Albania&quot;
;;;   &quot;3062&quot;
;;;   &quot;1851&quot;
;;;   &quot;0.05&quot;
;;;   &quot;0.05&quot;
;;;   &quot;10574&quot;
;;;   &quot;17497&quot;
;;;   &quot;0.02&quot;
;;;   &quot;3658&quot;
;;;   &quot;0.02&quot;
;;;   ...))
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; How many rows do we have?
;;; 
;; **

;; @@
(count rows)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>151</span>","value":"151"}
;; <=

;; **
;;; The column names can be taken from the first row. For convenience, we relpace some special characters and turn the names into clojure keywords.
;; **

;; @@
(def column-names
    (->> rows
         first
         (map (fn [s]
                  (-> s
                      (string/replace #"[\(|\)| |%]" "-")
                      keyword)))))

column-names
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:Country</span>","value":":Country"},{"type":"html","content":"<span class='clj-keyword'>:Population--1000s-</span>","value":":Population--1000s-"},{"type":"html","content":"<span class='clj-keyword'>:Adults--1000s-</span>","value":":Adults--1000s-"},{"type":"html","content":"<span class='clj-keyword'>:Share-of-world-population----</span>","value":":Share-of-world-population----"},{"type":"html","content":"<span class='clj-keyword'>:Share-of-adult-population----</span>","value":":Share-of-adult-population----"},{"type":"html","content":"<span class='clj-keyword'>:Wealth-per-capita</span>","value":":Wealth-per-capita"},{"type":"html","content":"<span class='clj-keyword'>:Wealth-per-adult</span>","value":":Wealth-per-adult"},{"type":"html","content":"<span class='clj-keyword'>:Share-of-world-wealth----</span>","value":":Share-of-world-wealth----"},{"type":"html","content":"<span class='clj-keyword'>:GDP-per-capita</span>","value":":GDP-per-capita"},{"type":"html","content":"<span class='clj-keyword'>:Share-of-world-GDP----</span>","value":":Share-of-world-GDP----"},{"type":"html","content":"<span class='clj-keyword'>:Wealth-Gini</span>","value":":Wealth-Gini"}],"value":"(:Country :Population--1000s- :Adults--1000s- :Share-of-world-population---- :Share-of-adult-population---- :Wealth-per-capita :Wealth-per-adult :Share-of-world-wealth---- :GDP-per-capita :Share-of-world-GDP---- ...)"}
;; <=

;; **
;;; Now we can represent the countries as maps whose keys are column names, and values are the respective cell values.
;; **

;; @@
(def all-countries
    (->> rows
         rest
         (map (fn [[country & numbers]]
                  (cons country
                        (->> numbers (map #(Double/parseDouble %))))))
         (map (fn [row]
                  (zipmap column-names row)))))
    
(->> all-countries
     (take 3)
     pprint)
;; @@
;; ->
;;; ({:Population--1000s- 6085576.0,
;;;   :Share-of-world-wealth---- 100.0,
;;;   :Wealth-per-adult 43494.0,
;;;   :Wealth-Gini 0.804,
;;;   :Share-of-adult-population---- 100.0,
;;;   :Share-of-world-population---- 100.0,
;;;   :GDP-per-capita 7675.0,
;;;   :Share-of-world-GDP---- 100.0,
;;;   :Country &quot;World&quot;,
;;;   :Wealth-per-capita 26416.0,
;;;   ...}
;;;  {:Population--1000s- 3062.0,
;;;   :Share-of-world-wealth---- 0.02,
;;;   :Wealth-per-adult 17497.0,
;;;   :Wealth-Gini 0.642,
;;;   :Share-of-adult-population---- 0.05,
;;;   :Share-of-world-population---- 0.05,
;;;   :GDP-per-capita 3658.0,
;;;   :Share-of-world-GDP---- 0.02,
;;;   :Country &quot;Albania&quot;,
;;;   :Wealth-per-capita 10574.0,
;;;   ...}
;;;  {:Population--1000s- 30463.0,
;;;   :Share-of-world-wealth---- 0.14,
;;;   :Wealth-per-adult 13635.0,
;;;   :Wealth-Gini 0.67,
;;;   :Share-of-adult-population---- 0.44,
;;;   :Share-of-world-population---- 0.5,
;;;   :GDP-per-capita 6107.0,
;;;   :Share-of-world-GDP---- 0.4,
;;;   :Country &quot;Algeria&quot;,
;;;   :Wealth-per-capita 7320.0,
;;;   ...})
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; One column that may interest us is the wealth [Gini coefficient](https://en.wikipedia.org/wiki/Gini_coefficient) (measuring wealth inequality).
;;; 
;;; Let us sort by this column, take some examples, and print them as a table.
;;; 
;;; These are the countries of lowest Gini coefficients (which means less inequality in terms of wealth). Note, however, that these data are [not up to date](https://en.wikipedia.org/wiki/List_of_countries_by_distribution_of_wealth).
;; **

;; @@
(->> all-countries
     (map (fn [m]
              (select-keys m [:Country :Wealth-Gini])))
     (sort-by :Wealth-Gini)
     (take 10)
     print-table)
;; @@
;; ->
;;; 
;;; |    :Country | :Wealth-Gini |
;;; |-------------+--------------|
;;; |       Japan |        0.547 |
;;; |       China |         0.55 |
;;; |       Spain |         0.57 |
;;; | South Korea |        0.579 |
;;; |       Macau |         0.58 |
;;; |     Ireland |        0.581 |
;;; |       Italy |        0.609 |
;;; |       Yemen |        0.613 |
;;; |     Finland |        0.615 |
;;; |      Brazil |         0.62 |
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Let us plot countries by their wealth-per-capita on one axis, and by the wealth-Gini on the other.
;;; 
;;; We will use gorilla-plot, which is Gorilla-REPL's own plotting library, based on [Vega](https://vega.github.io/vega/).
;;; 
;;; First, we define a function that can plot a given sequence of countries with a given color.
;; **

;; @@
(defn plot-countries [color countries]
  (list-plot (map (juxt :Wealth-per-capita :Wealth-Gini)
                  countries)
             :color color
             :x-title "Wealth per capita"
             :y-title "Wealth Gini"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;examples.scraping-wikipedia/plot-countries</span>","value":"#'examples.scraping-wikipedia/plot-countries"}
;; <=

;; **
;;; Now, let us plot all countries in purple:
;; **

;; @@
(->> all-countries
     (plot-countries :purple))
;; @@
;; =>
;;; {"type":"vega","content":{"width":400,"height":247.2188,"padding":{"top":10,"left":55,"bottom":40,"right":10},"data":[{"name":"a6bdb794-e51a-4fb9-9a65-ec3015337209","values":[{"x":26416.0,"y":0.804},{"x":10574.0,"y":0.642},{"x":7320.0,"y":0.67},{"x":20944.0,"y":0.747},{"x":36740.0,"y":0.74},{"x":9480.0,"y":0.684},{"x":90906.0,"y":0.622},{"x":73047.0,"y":0.646},{"x":6737.0,"y":0.678},{"x":6305.0,"y":0.66},{"x":102932.0,"y":0.706},{"x":14659.0,"y":0.628},{"x":86208.0,"y":0.662},{"x":12550.0,"y":0.763},{"x":3378.0,"y":0.713},{"x":6654.0,"y":0.762},{"x":15719.0,"y":0.751},{"x":62676.0,"y":0.62},{"x":15120.0,"y":0.652},{"x":2123.0,"y":0.728},{"x":1876.0,"y":0.699},{"x":4890.0,"y":0.714},{"x":5290.0,"y":0.711},{"x":89252.0,"y":0.688},{"x":10801.0,"y":0.688},{"x":1949.0,"y":0.782},{"x":1726.0,"y":0.681},{"x":27536.0,"y":0.777},{"x":11267.0,"y":0.55},{"x":13826.0,"y":0.765},{"x":5182.0,"y":0.711},{"x":1400.0,"y":0.711},{"x":2806.0,"y":0.711},{"x":14718.0,"y":0.732},{"x":5212.0,"y":0.712},{"x":22021.0,"y":0.654},{"x":32431.0,"y":0.626},{"x":66191.0,"y":0.808},{"x":12717.0,"y":0.763},{"x":13873.0,"y":0.723},{"x":6758.0,"y":0.76},{"x":15541.0,"y":0.689},{"x":18408.0,"y":0.746},{"x":7404.0,"y":0.688},{"x":24556.0,"y":0.675},{"x":1412.0,"y":0.652},{"x":9928.0,"y":0.709},{"x":53154.0,"y":0.615},{"x":94557.0,"y":0.73},{"x":14833.0,"y":0.784},{"x":3894.0,"y":0.723},{"x":12358.0,"y":0.725},{"x":90768.0,"y":0.667},{"x":3903.0,"y":0.692},{"x":69855.0,"y":0.654},{"x":15250.0,"y":0.763},{"x":12858.0,"y":0.779},{"x":1673.0,"y":0.71},{"x":7756.0,"y":0.693},{"x":5697.0,"y":0.707},{"x":6244.0,"y":0.755},{"x":5318.0,"y":0.743},{"x":188699.0,"y":0.74},{"x":31452.0,"y":0.651},{"x":81945.0,"y":0.664},{"x":6513.0,"y":0.669},{"x":7973.0,"y":0.764},{"x":16673.0,"y":0.707},{"x":91432.0,"y":0.581},{"x":64633.0,"y":0.677},{"x":120897.0,"y":0.609},{"x":9601.0,"y":0.686},{"x":124858.0,"y":0.547},{"x":10792.0,"y":0.678},{"x":13723.0,"y":0.655},{"x":3442.0,"y":0.699},{"x":5174.0,"y":0.68},{"x":18958.0,"y":0.67},{"x":20560.0,"y":0.762},{"x":2876.0,"y":0.767},{"x":21566.0,"y":0.666},{"x":185231.0,"y":0.65},{"x":71660.0,"y":0.58},{"x":14759.0,"y":0.661},{"x":2226.0,"y":0.722},{"x":2559.0,"y":0.736},{"x":12458.0,"y":0.733},{"x":1798.0,"y":0.75},{"x":74246.0,"y":0.664},{"x":3966.0,"y":0.686},{"x":60398.0,"y":0.661},{"x":23488.0,"y":0.749},{"x":7790.0,"y":0.691},{"x":12440.0,"y":0.69},{"x":2820.0,"y":0.689},{"x":8843.0,"y":0.847},{"x":121165.0,"y":0.65},{"x":55823.0,"y":0.651},{"x":5161.0,"y":0.755},{"x":1755.0,"y":0.729},{"x":905.0,"y":0.736},{"x":79292.0,"y":0.633},{"x":5987.0,"y":0.698},{"x":15003.0,"y":0.766},{"x":3629.0,"y":0.738},{"x":10879.0,"y":0.766},{"x":11577.0,"y":0.738},{"x":12453.0,"y":0.717},{"x":24654.0,"y":0.657},{"x":53811.0,"y":0.667},{"x":77876.0,"y":0.753},{"x":14806.0,"y":0.651},{"x":16579.0,"y":0.699},{"x":2955.0,"y":0.714},{"x":3235.0,"y":0.711},{"x":22025.0,"y":0.737},{"x":4309.0,"y":0.697},{"x":26486.0,"y":0.76},{"x":2043.0,"y":0.687},{"x":113632.0,"y":0.689},{"x":24049.0,"y":0.629},{"x":37019.0,"y":0.626},{"x":16266.0,"y":0.763},{"x":45278.0,"y":0.579},{"x":93086.0,"y":0.57},{"x":10337.0,"y":0.665},{"x":22339.0,"y":0.763},{"x":18013.0,"y":0.763},{"x":13287.0,"y":0.741},{"x":12773.0,"y":0.78},{"x":78148.0,"y":0.742},{"x":137549.0,"y":0.803},{"x":8917.0,"y":0.704},{"x":100009.0,"y":0.655},{"x":2940.0,"y":0.664},{"x":1216.0,"y":0.676},{"x":13920.0,"y":0.71},{"x":2217.0,"y":0.711},{"x":51101.0,"y":0.689},{"x":20534.0,"y":0.693},{"x":22379.0,"y":0.718},{"x":2889.0,"y":0.723},{"x":128959.0,"y":0.697},{"x":9547.0,"y":0.667},{"x":20926.0,"y":0.708},{"x":143727.0,"y":0.801},{"x":14711.0,"y":0.712},{"x":5621.0,"y":0.682},{"x":1426.0,"y":0.613},{"x":2010.0,"y":0.766}]}],"marks":[{"type":"symbol","from":{"data":"a6bdb794-e51a-4fb9-9a65-ec3015337209"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"purple"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}}],"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"a6bdb794-e51a-4fb9-9a65-ec3015337209","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"a6bdb794-e51a-4fb9-9a65-ec3015337209","field":"data.y"}}],"axes":[{"type":"x","scale":"x","title":"Wealth-per-capita","titleOffset":30},{"type":"y","scale":"y","title":"Wealth-Gini","titleOffset":45}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :data [{:name \"a6bdb794-e51a-4fb9-9a65-ec3015337209\", :values (# # # # # # # # # # ...)}], :marks [{:type \"symbol\", :from {:data \"a6bdb794-e51a-4fb9-9a65-ec3015337209\"}, :properties {:enter #, :update #, :hover #}}], :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"a6bdb794-e51a-4fb9-9a65-ec3015337209\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"a6bdb794-e51a-4fb9-9a65-ec3015337209\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\", :title :Wealth-per-capita, :titleOffset 30} {:type \"y\", :scale \"y\", :title :Wealth-Gini, :titleOffset 45}]}}"}
;; <=

;; **
;;; Now, let us add Japan in red and Yemen in orange.
;; **

;; @@
(compose (->> all-countries
              (plot-countries :purple))
         (->> all-countries
              (filter #(-> % :Country (= "Japan")))
              (plot-countries :red))
         (->> all-countries
              (filter #(-> % :Country (= "Yemen")))
              (plot-countries :orange)))
;; @@
;; =>
;;; {"type":"vega","content":{"width":400,"height":247.2188,"padding":{"top":10,"left":55,"bottom":40,"right":10},"scales":[{"name":"x","type":"linear","range":"width","zero":false,"domain":{"data":"844fdfe6-6519-43a9-82c1-56050451e232","field":"data.x"}},{"name":"y","type":"linear","range":"height","nice":true,"zero":false,"domain":{"data":"844fdfe6-6519-43a9-82c1-56050451e232","field":"data.y"}}],"axes":[{"type":"x","scale":"x","title":"Wealth-per-capita","titleOffset":30},{"type":"y","scale":"y","title":"Wealth-Gini","titleOffset":45}],"data":[{"name":"844fdfe6-6519-43a9-82c1-56050451e232","values":[{"x":26416.0,"y":0.804},{"x":10574.0,"y":0.642},{"x":7320.0,"y":0.67},{"x":20944.0,"y":0.747},{"x":36740.0,"y":0.74},{"x":9480.0,"y":0.684},{"x":90906.0,"y":0.622},{"x":73047.0,"y":0.646},{"x":6737.0,"y":0.678},{"x":6305.0,"y":0.66},{"x":102932.0,"y":0.706},{"x":14659.0,"y":0.628},{"x":86208.0,"y":0.662},{"x":12550.0,"y":0.763},{"x":3378.0,"y":0.713},{"x":6654.0,"y":0.762},{"x":15719.0,"y":0.751},{"x":62676.0,"y":0.62},{"x":15120.0,"y":0.652},{"x":2123.0,"y":0.728},{"x":1876.0,"y":0.699},{"x":4890.0,"y":0.714},{"x":5290.0,"y":0.711},{"x":89252.0,"y":0.688},{"x":10801.0,"y":0.688},{"x":1949.0,"y":0.782},{"x":1726.0,"y":0.681},{"x":27536.0,"y":0.777},{"x":11267.0,"y":0.55},{"x":13826.0,"y":0.765},{"x":5182.0,"y":0.711},{"x":1400.0,"y":0.711},{"x":2806.0,"y":0.711},{"x":14718.0,"y":0.732},{"x":5212.0,"y":0.712},{"x":22021.0,"y":0.654},{"x":32431.0,"y":0.626},{"x":66191.0,"y":0.808},{"x":12717.0,"y":0.763},{"x":13873.0,"y":0.723},{"x":6758.0,"y":0.76},{"x":15541.0,"y":0.689},{"x":18408.0,"y":0.746},{"x":7404.0,"y":0.688},{"x":24556.0,"y":0.675},{"x":1412.0,"y":0.652},{"x":9928.0,"y":0.709},{"x":53154.0,"y":0.615},{"x":94557.0,"y":0.73},{"x":14833.0,"y":0.784},{"x":3894.0,"y":0.723},{"x":12358.0,"y":0.725},{"x":90768.0,"y":0.667},{"x":3903.0,"y":0.692},{"x":69855.0,"y":0.654},{"x":15250.0,"y":0.763},{"x":12858.0,"y":0.779},{"x":1673.0,"y":0.71},{"x":7756.0,"y":0.693},{"x":5697.0,"y":0.707},{"x":6244.0,"y":0.755},{"x":5318.0,"y":0.743},{"x":188699.0,"y":0.74},{"x":31452.0,"y":0.651},{"x":81945.0,"y":0.664},{"x":6513.0,"y":0.669},{"x":7973.0,"y":0.764},{"x":16673.0,"y":0.707},{"x":91432.0,"y":0.581},{"x":64633.0,"y":0.677},{"x":120897.0,"y":0.609},{"x":9601.0,"y":0.686},{"x":124858.0,"y":0.547},{"x":10792.0,"y":0.678},{"x":13723.0,"y":0.655},{"x":3442.0,"y":0.699},{"x":5174.0,"y":0.68},{"x":18958.0,"y":0.67},{"x":20560.0,"y":0.762},{"x":2876.0,"y":0.767},{"x":21566.0,"y":0.666},{"x":185231.0,"y":0.65},{"x":71660.0,"y":0.58},{"x":14759.0,"y":0.661},{"x":2226.0,"y":0.722},{"x":2559.0,"y":0.736},{"x":12458.0,"y":0.733},{"x":1798.0,"y":0.75},{"x":74246.0,"y":0.664},{"x":3966.0,"y":0.686},{"x":60398.0,"y":0.661},{"x":23488.0,"y":0.749},{"x":7790.0,"y":0.691},{"x":12440.0,"y":0.69},{"x":2820.0,"y":0.689},{"x":8843.0,"y":0.847},{"x":121165.0,"y":0.65},{"x":55823.0,"y":0.651},{"x":5161.0,"y":0.755},{"x":1755.0,"y":0.729},{"x":905.0,"y":0.736},{"x":79292.0,"y":0.633},{"x":5987.0,"y":0.698},{"x":15003.0,"y":0.766},{"x":3629.0,"y":0.738},{"x":10879.0,"y":0.766},{"x":11577.0,"y":0.738},{"x":12453.0,"y":0.717},{"x":24654.0,"y":0.657},{"x":53811.0,"y":0.667},{"x":77876.0,"y":0.753},{"x":14806.0,"y":0.651},{"x":16579.0,"y":0.699},{"x":2955.0,"y":0.714},{"x":3235.0,"y":0.711},{"x":22025.0,"y":0.737},{"x":4309.0,"y":0.697},{"x":26486.0,"y":0.76},{"x":2043.0,"y":0.687},{"x":113632.0,"y":0.689},{"x":24049.0,"y":0.629},{"x":37019.0,"y":0.626},{"x":16266.0,"y":0.763},{"x":45278.0,"y":0.579},{"x":93086.0,"y":0.57},{"x":10337.0,"y":0.665},{"x":22339.0,"y":0.763},{"x":18013.0,"y":0.763},{"x":13287.0,"y":0.741},{"x":12773.0,"y":0.78},{"x":78148.0,"y":0.742},{"x":137549.0,"y":0.803},{"x":8917.0,"y":0.704},{"x":100009.0,"y":0.655},{"x":2940.0,"y":0.664},{"x":1216.0,"y":0.676},{"x":13920.0,"y":0.71},{"x":2217.0,"y":0.711},{"x":51101.0,"y":0.689},{"x":20534.0,"y":0.693},{"x":22379.0,"y":0.718},{"x":2889.0,"y":0.723},{"x":128959.0,"y":0.697},{"x":9547.0,"y":0.667},{"x":20926.0,"y":0.708},{"x":143727.0,"y":0.801},{"x":14711.0,"y":0.712},{"x":5621.0,"y":0.682},{"x":1426.0,"y":0.613},{"x":2010.0,"y":0.766}]},{"name":"d04f8693-abc6-4f58-96df-f86eacea28ea","values":[{"x":124858.0,"y":0.547}]},{"name":"a2a61eac-47cc-4fe1-847f-016537c76ddb","values":[{"x":1426.0,"y":0.613}]}],"marks":[{"type":"symbol","from":{"data":"844fdfe6-6519-43a9-82c1-56050451e232"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"purple"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}},{"type":"symbol","from":{"data":"d04f8693-abc6-4f58-96df-f86eacea28ea"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"red"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}},{"type":"symbol","from":{"data":"a2a61eac-47cc-4fe1-847f-016537c76ddb"},"properties":{"enter":{"x":{"scale":"x","field":"data.x"},"y":{"scale":"y","field":"data.y"},"fill":{"value":"orange"},"fillOpacity":{"value":1}},"update":{"shape":"circle","size":{"value":70},"stroke":{"value":"transparent"}},"hover":{"size":{"value":210},"stroke":{"value":"white"}}}}]},"value":"#gorilla_repl.vega.VegaView{:content {:width 400, :height 247.2188, :padding {:top 10, :left 55, :bottom 40, :right 10}, :scales [{:name \"x\", :type \"linear\", :range \"width\", :zero false, :domain {:data \"844fdfe6-6519-43a9-82c1-56050451e232\", :field \"data.x\"}} {:name \"y\", :type \"linear\", :range \"height\", :nice true, :zero false, :domain {:data \"844fdfe6-6519-43a9-82c1-56050451e232\", :field \"data.y\"}}], :axes [{:type \"x\", :scale \"x\", :title :Wealth-per-capita, :titleOffset 30} {:type \"y\", :scale \"y\", :title :Wealth-Gini, :titleOffset 45}], :data ({:name \"844fdfe6-6519-43a9-82c1-56050451e232\", :values (# # # # # # # # # # ...)} {:name \"d04f8693-abc6-4f58-96df-f86eacea28ea\", :values (#)} {:name \"a2a61eac-47cc-4fe1-847f-016537c76ddb\", :values (#)}), :marks ({:type \"symbol\", :from {:data \"844fdfe6-6519-43a9-82c1-56050451e232\"}, :properties {:enter #, :update #, :hover #}} {:type \"symbol\", :from {:data \"d04f8693-abc6-4f58-96df-f86eacea28ea\"}, :properties {:enter #, :update #, :hover #}} {:type \"symbol\", :from {:data \"a2a61eac-47cc-4fe1-847f-016537c76ddb\"}, :properties {:enter #, :update #, :hover #}})}}"}
;; <=

;; **
;;; So, Japan and Yemen both has low wealth Gini coefficient, but are very different in terms of their wealth per capita.
;;; 
;;; Note how we composed several plots in a simple manner. If you find this interesting, look in to Jony Hudson's [videos](http://gorilla-repl.org/videos.html) about Gorilla REPL.
;; **

;; @@

;; @@
