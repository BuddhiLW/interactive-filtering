(ns webcam.filters
  (:require
    [opencv4.core :refer :all]))

(defn process-frame [frame]
  "Applies filters to the frame. Modify this function during development."
  (-> frame
      (cvt-color! COLOR_BGR2GRAY)
      (cvt-color! COLOR_GRAY2BGR)
      (bitwise-not!)
      (gaussian-blur! (new-size 17 17) 9 9)))
