(ns webcam.core
  (:require
   [webcam.filters :as filter]
   [clojure.tools.namespace.repl :as ns-repl])
  (:import [org.bytedeco.javacv FrameGrabber FrameRecorder]
           [org.bytedeco.javacv OpenCVFrameGrabber OpenCVFrameRecorder]))

(def running? (atom true))

(defn process-frame [frame]
  "Dynamically calls the currently loaded frame processing function."
  (filter/process-frame frame)) ;; Reloadable filter function

(defn start-pipeline []
  (let [grabber (OpenCVFrameGrabber. 3)  ;; /dev/video0
        recorder (OpenCVFrameRecorder/createDefault "/dev/video3" 640 480)] ;; Virtual cam output

    (.start grabber)
    (.start recorder)

    (future
      (while @running?
        (when-let [frame (.grab grabber)]
          (let [processed-frame (process-frame frame)]
            (.record recorder processed-frame)))))

    {:grabber grabber :recorder recorder}))

(defn stop-pipeline [pipeline]
  "Stops the capture and recording process."
  (.stop (:grabber pipeline))
  (.stop (:recorder pipeline))
  (reset! running? false))

(defn reload []
  "Reloads filter functions without restarting the whole pipeline."
  (ns-repl/refresh))

(comment
  ;; Start the video processing pipeline
  (def pipeline (start-pipeline))

  ;; Reload filters without stopping the pipeline
  (reload)

  ;; Stop the pipeline
  (stop-pipeline pipeline))
