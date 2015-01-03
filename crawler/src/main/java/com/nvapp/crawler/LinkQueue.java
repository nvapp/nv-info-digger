package com.nvapp.crawler;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Queue;

public class LinkQueue {
    private static Set visitedUrl = new HashSet();
    private static Queue unVisitedUrl = new PriorityQueue();

    public static Queue getUnVisitedUrl() {
        return unVisitedUrl;
    }

    public static void addVisitedUrl(String url) {
        visitedUrl.add(url);
    }

    public static void removeVisitedUrl(String url) {
        visitedUrl.remove(url);
    }

    public static Object unVisitedUrlDeQueue() {
        return unVisitedUrl.poll();
    }

    public static void addUnvisitedUrl(String url) {
        if (url != null && !url.trim().equals("") && !visitedUrl.contains(url) && !unVisitedUrl.contains(url))
            unVisitedUrl.add(url);
    }

    public static int getVisitedUrlNum() {
        return visitedUrl.size();
    }

    public static boolean unVisitedUrlsEmpty() {
        return unVisitedUrl.isEmpty();
    }

}
