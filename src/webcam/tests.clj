(ns webcam.tests
   (:require
    [opencv4.core :refer :all]
    [opencv4.utils :as u]))


(comment
  (defn show-window []
    (let [frame (JFrame. "Hello Swing")]
      (.add frame (JLabel. "Hi there!"))
      (.setSize frame 300 200)
      (.setVisible frame true)))

  (show-window))

(comment
 (u/simple-cam-window
  {:video {:device 1}
   :frame {:fps true}}
  (fn [buffer]
   (u/resize-by buffer 0.5)
   (let [output (new-mat) bottom (-> buffer clone (flip! -1))]
    (-> buffer (cvt-color! COLOR_RGB2GRAY) (cvt-color! COLOR_GRAY2RGB))
    (put-text buffer (str (java.util.Date.)) (new-point 10 50) FONT_HERSHEY_PLAIN 1 (new-scalar 255 255 0) 1)
    (vconcat [buffer bottom] output)
    output)))
 ;;
 (u/simple-cam-window
  { ;; :video {:device 1}
   :frame {:fps true}}  ; device index 1 => typically /dev/video1
  [{:class origami.filters.Cartoon}]))

(comment
 (-> "resources/lena.png"
     (imread)
     (gaussian-blur! (new-size 17 17) 9 9)
     (imwrite "resources/blurred.png")))

(defn main []
  (println "hello"))

(ns webcam.pipeline
  (:require
    [opencv4.core :refer :all]
    [opencv4.utils :as u]))
