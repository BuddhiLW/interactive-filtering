(defproject webcam "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :repositories [["hellonico" "https://repository.hellonico.info/repository/hellonico"]]
  :dependencies [[org.clojure/clojure "1.12.0"]
                 ;; [org.freedesktop.gstreamer/gst1-java-core "1.4.0"
                 ;;   :exclusions [net.java.dev.jna/jna]]
                 ;; [org.freedesktop.gstreamer/gst1-java-platform ""]
                 [net.java.dev.jna/jna "5.8.0"]
                 [origami/origami "4.9.0-8"]
                 [origami-dnn "0.1.19"]
                 [org.bytedeco/javacv-platform "1.5.11"]
                 [org.clojure/tools.namespace "1.5.0"]
                 ;; https://mvnrepository.com/artifact/origami/filters
                 [origami/filters "1.48"]]

  :jvm-opts ["-Dclojure.compile.path=~/.m2/repository"
             "-Xbootclasspath/a:~/.m2/repository"
             "-Djava.awt.headless=false"]
  :repl-options {:init-ns webcam.core})
