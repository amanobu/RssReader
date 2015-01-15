package ama.rssreader.util;

import ama.rssreader.entities.Contentstbl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class RssUtil {

    public static void main(String args[]) throws Exception {
        String url = "http://rss.rssad.jp/rss/gihyo/feed/rss1";
        // String url = "http://rss.rssad.jp/rss/gihyo/feed/rss2";
        // String url = "http://rss.rssad.jp/rss/gihyo/feed/atom";

        URL feedUrl = new URL(url);
        SyndFeedInput input = new SyndFeedInput();

        SyndFeed feed = input.build(new XmlReader(feedUrl));
        // サイトのタイトル
        System.out.println(feed.getTitle());
        // サイトのURL
        System.out.println(feed.getLink());
        for (Object obj : feed.getEntries()) {
            SyndEntry entry = (SyndEntry) obj;
            // 記事タイトル
            System.out.println("title:" + entry.getTitle());
            // 記事のURL
            System.out.println("url:" + entry.getLink());
            // 記事の詳細//
            System.out.println("contents:" + entry.getDescription().getValue());
            // 記事の投稿日
            System.out.println("publicsh:" + entry.getPublishedDate());
            // 記事の投稿日
            System.out.println("update date:" + entry.getUpdatedDate());
        }
    }

    public static String getTitle(String url) throws Exception {
        URL feedUrl = new URL(url);
        SyndFeedInput input = new SyndFeedInput();

        SyndFeed feed = input.build(new XmlReader(feedUrl));
        return feed.getTitle();
    }

    public static List<Contentstbl> getFeeds(String url) throws Exception {
        URL feedUrl = new URL(url);
        SyndFeedInput input = new SyndFeedInput();

        SyndFeed feed = input.build(new XmlReader(feedUrl));
        List<Contentstbl> list = new LinkedList<>();
        for (SyndEntry obj : feed.getEntries()) {
            Contentstbl rssfeed = new Contentstbl();
            SyndEntry entry = (SyndEntry) obj;
            rssfeed.setTitle(entry.getTitle());
            rssfeed.setUrl(entry.getLink());
            String description = "";
            if (null != entry.getDescription()) {
                description = entry.getDescription().getValue();
                if (null != description && description.length() > 32672) {
                    //DBの長さまで切り詰める
                    description = description.substring(0, 32671);
                }
            }
            rssfeed.setContents(description);
            
            //rssfeed.setUpddate(entry.getUpdatedDate());
            rssfeed.setPublishdate(entry.getPublishedDate());
            //LogUtil.log(RssUtil.class.getName(), Level.FINE, "feed::::::::", entry.getTitle(), entry.getLink(), entry.getDescription(), description,entry.getUpdatedDate(),entry.getPublishedDate());
            list.add(rssfeed);
        }
        return list;
    }
}
