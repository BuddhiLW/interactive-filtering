(ns webcam.v4l2
  (:require [clojure.java.shell :refer [sh]]))

(defn start-v4l2-output []
  (sh "ffmpeg"
      "-f" "v4l2"
      "-i" "/dev/video1"
      "-pix_fmt" "yuyv422"
      "-vf" "format=yuyv422"
      "-f" "v4l2"
      "/dev/video0"))

(comment
  (start-v4l2-output)

  ;;
  (sh "ffmpeg" "-f" "rawvideo" "-pixel_format" "bgr24" "-video_size" "640x480"
        "-i" "-" "-f" "v4l2" "/dev/video2"))
;; => {:exit 234, :out "", :err "ffmpeg version 6.1.1-3ubuntu5 Copyright (c) 2000-2023 the FFmpeg developers\n  built with gcc 13 (Ubuntu 13.2.0-23ubuntu3)\n  configuration: --prefix=/usr --extra-version=3ubuntu5 --toolchain=hardened --libdir=/usr/lib/x86_64-linux-gnu --incdir=/usr/include/x86_64-linux-gnu --arch=amd64 --enable-gpl --disable-stripping --disable-omx --enable-gnutls --enable-libaom --enable-libass --enable-libbs2b --enable-libcaca --enable-libcdio --enable-libcodec2 --enable-libdav1d --enable-libflite --enable-libfontconfig --enable-libfreetype --enable-libfribidi --enable-libglslang --enable-libgme --enable-libgsm --enable-libharfbuzz --enable-libmp3lame --enable-libmysofa --enable-libopenjpeg --enable-libopenmpt --enable-libopus --enable-librubberband --enable-libshine --enable-libsnappy --enable-libsoxr --enable-libspeex --enable-libtheora --enable-libtwolame --enable-libvidstab --enable-libvorbis --enable-libvpx --enable-libwebp --enable-libx265 --enable-libxml2 --enable-libxvid --enable-libzimg --enable-openal --enable-opencl --enable-opengl --disable-sndio --enable-libvpl --disable-libmfx --enable-libdc1394 --enable-libdrm --enable-libiec61883 --enable-chromaprint --enable-frei0r --enable-ladspa --enable-libbluray --enable-libjack --enable-libpulse --enable-librabbitmq --enable-librist --enable-libsrt --enable-libssh --enable-libsvtav1 --enable-libx264 --enable-libzmq --enable-libzvbi --enable-lv2 --enable-sdl2 --enable-libplacebo --enable-librav1e --enable-pocketsphinx --enable-librsvg --enable-libjxl --enable-shared\n  libavutil      58. 29.100 / 58. 29.100\n  libavcodec     60. 31.102 / 60. 31.102\n  libavformat    60. 16.100 / 60. 16.100\n  libavdevice    60.  3.100 / 60.  3.100\n  libavfilter     9. 12.100 /  9. 12.100\n  libswscale      7.  5.100 /  7.  5.100\n  libswresample   4. 12.100 /  4. 12.100\n  libpostproc    57.  3.100 / 57.  3.100\nInput #0, rawvideo, from 'fd:':\n  Duration: N/A, bitrate: 184320 kb/s\n  Stream #0:0: Video: rawvideo (BGR[24] / 0x18524742), bgr24, 640x480, 184320 kb/s, 25 tbr, 25 tbn\nStream mapping:\n  Stream #0:0 -> #0:0 (rawvideo (native) -> rawvideo (native))\n[vost#0:0/rawvideo @ 0x5575e7eb0a40] No filtered frames for output stream, trying to initialize anyway.\n[video4linux2,v4l2 @ 0x5575e7eb0240] ioctl(VIDIOC_G_FMT): Invalid argument\n[out#0/video4linux2,v4l2 @ 0x5575e7eb0140] Could not write header (incorrect codec parameters ?): Invalid argument\n[out#0/video4linux2,v4l2 @ 0x5575e7eb0140] Nothing was written into output file, because at least one of its streams received no packets.\nframe=    0 fps=0.0 q=0.0 Lsize=       0kB time=N/A bitrate=N/A speed=N/A    \nConversion failed!\n"}
      ;; => "/dev/video2")
