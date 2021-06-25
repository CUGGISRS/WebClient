package com.lomoye.easy.utils;

import com.lomoye.easy.component.DefaultHttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

/**
 * 2020/9/6 14:22
 * yechangjun
 */
public class HttpUtil {

    private static DefaultHttpClientDownloader httpClientDownloader = new DefaultHttpClientDownloader();

    public static Html download(String url) {
        return httpClientDownloader.download(url);
    }

    public static void main(String[] args) {
        System.out.println(httpClientDownloader.download("https://movie.douban.com/top250?start=0&filter="));
    }
}
