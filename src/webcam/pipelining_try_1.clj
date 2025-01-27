(ns webcam.pipelining-try-1
   (:require
    [opencv4.core :refer :all]
    [opencv4.utils :as u]))

;; -----------------------------------------
;; 1) IMAGE MASK
;; -----------------------------------------
(defn make-mask
  "Create a binary mask from the input frame.
   Here, we just threshold the grayscale image at 128.
   The result is a single-channel mask that can be used
   with bitwise-and or alpha merging."
  [mat]
  (let [gray (-> mat
                 (clone)
                 (cvt-color! COLOR_BGR2GRAY))
        mask (new-mat)]
    ;; Simple threshold: everything above 128 => white(255), else black(0)
    (threshold gray mask 128 255 THRESH_BINARY)
    mask))

(defn apply-image-mask
  "Apply the mask to the original mat.
   By default, bitwise-and mat with itself, using mask."
  [mat]
  (let [mask (make-mask mat)
        output (new-mat)]
    (bitwise-and mat mat output mask)
    output))

;; -----------------------------------------
;; 2) COLOR CORRECTION
;; -----------------------------------------
(defn color-correction
  "A simple color-correction function to change brightness and contrast,
   or do something more advanced if you like."
  [mat]
  (let [output (new-mat)]
    ;; For example, add alpha * mat + beta
    ;; alpha=1.2 => ~20% brighter, beta=20 => shift brightness
    (convert-scale-abs mat output 1.2 20)
    output))

;; -----------------------------------------
;; 3) LUMA KEY
;; -----------------------------------------
(defn luma-key
  "A trivial luma key that sets pixels below a certain gray level to black
   (or you can do alpha compositing if you want a more advanced approach)."
  [mat]
  (let [gray (-> mat
                 (clone)
                 (cvt-color! COLOR_BGR2GRAY))
        keyed (clone mat)]
    (doseq [row (range (.rows gray))
            col (range (.cols gray))]
      (let [l (double (.get gray row col 0))]
        ;; For instance, if l < 50 => set pixel to black
        (when (< l 50)
          (.put keyed row col (into-array Double [0 0 0])))))
    keyed))

;; -----------------------------------------
;; ASSEMBLE THE PIPELINE
;; -----------------------------------------
(defn pipeline
  "Apply a sequence of transformations in order:
   1) Mask
   2) Color-correction
   3) Luma-key"
  [frame]
  (-> frame
      (apply-image-mask)
      (color-correction)
      (luma-key)))

;; -----------------------------------------
;; USE IN A SIMPLE CAM WINDOW
;; -----------------------------------------
(defn start-pipeline []
  (u/simple-cam-window
    {:video {:device 3} ; pick the correct device on your system
     :frame {:fps true}}
    (fn [buffer]
      ;; pipeline is the sequence of transformations:
      (pipeline buffer))))

(comment
  ;; To run, evaluate
  (start-pipeline)
  (def a (u/simple-cam-window
          {:video {:device 8}} ; pick the correct device on your system
          "[{:class origami.filters.Canny, :inverted true, :threshold1 50, :threshold2 200}]")))
