(ns webcam.realtime
  (:require
    [opencv4.core :refer :all]
    [opencv4.utils :as u]))

;; An atom to store the list of filters (functions).
(def pipeline (atom []))

(defn add-filter
  "Add a new filter function to the end of the pipeline."
  [f]
  (swap! pipeline conj f)
  (println "Filter added:" f))

(defn remove-filter
  "Remove a filter from the pipeline. We match by equality (`=`).
   If you have the same function added multiple times, you could
   refine this logic or remove them all."
  [f]
  (swap! pipeline #(remove #{f} %))
  (println "Filter removed:" f))

(defn clear-filters []
  "Clear all filters from the pipeline."
  (reset! pipeline [])
  (println "All filters removed."))

(defn apply-pipeline [mat]
  "Applies each filter in @pipeline to the incoming frame, in order."
  (println "Current filters in pipeline:" (count @pipeline))
  (reduce (fn [m f] (f m)) mat @pipeline))

;; Example filters
(defn filter-grayscale
  "Convert to grayscale (then back to BGR so downstream filters
   still see a 3-channel image)."
  [mat]
  (-> mat
      (cvt-color! COLOR_BGR2GRAY)
      (cvt-color! COLOR_GRAY2BGR)))

(defn filter-invert
  "Invert the pixel values (like a negative)."
  [mat]
  (bitwise-not! mat))

(defn filter-blur
  "Apply a Gaussian blur."
  [mat]
  (gaussian-blur! mat (new-size 15 15) 0 0))

;; Variable to hold the camera window reference
(def cam-window (atom nil))

(defn start-stream
  "Open a simple webcam window. The frame function applies our
   (dynamic) pipeline to each new camera frame."
  []
  (when @cam-window
    (println "Stream is already running."))
  (reset! cam-window
          (u/simple-cam-window
            {:video {:device 8}  ;; Change if needed
             :frame {:fps true}}
            (fn [buffer]
              (let [processed (apply-pipeline buffer)]
                processed))))
  (println "Camera stream started."))

(defn stop-stream
  "Stops the currently running webcam stream."
  []
  (when @cam-window
    (println "Stopping camera stream...")
    (.putClientProperty @cam-window "quit" true)
    (.dispose @cam-window)
    (reset! cam-window nil)
    (println "Camera stream stopped."))
  (println "No active stream to stop."))

(comment
  ;; Start the camera feed
  (start-stream)

  ;; Stop the camera feed
  (stop-stream)

  ;; Add filters dynamically
  (add-filter filter-grayscale)   ;; Grayscale effect
  (add-filter filter-invert)      ;; Inverted colors
  (add-filter filter-blur)        ;; Gaussian blur

  ;; Remove specific filters
  (remove-filter filter-blur)
  (remove-filter filter-invert)

  ;; Clear all filters
  (clear-filters)

  ;; Add a custom invert function
  (defn invert-colors [mat] (bitwise-not! mat))
  (add-filter invert-colors)

  ;; Remove the custom function
  (remove-filter invert-colors))
