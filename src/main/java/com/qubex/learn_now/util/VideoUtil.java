package com.qubex.learn_now.util;

public class VideoUtil {

    public static String convertToEmbedUrl(String url) {
        if (url.contains("youtube.com/watch?v=")) {
            String videoId = url.substring(url.indexOf("v=") + 2);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
            return "https://www.youtube.com/embed/" + videoId;
        }

        if (url.contains("youtu.be/")) {
            String videoId = url.substring(url.lastIndexOf("/") + 1);
            return "https://www.youtube.com/embed/" + videoId;
        }

        if (url.contains("vimeo.com/")) {
            String videoId = url.substring(url.lastIndexOf("/") + 1);
            return "https://player.vimeo.com/video/" + videoId;
        }

        // Default: return original
        return url;
    }
}

