package ama.rssreader.util;

import ama.rssreader.entities.Contentstbl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Named;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 * 各種Utilクラス
 *
 * @Named(value = "utilBean")としたのはJSFから呼べるようにした為
 * @author amanobu
 */
@Named(value = "utilBean")
public class RssUtil {

    //ホワイトリスト
    private static final Whitelist WHITELIST = buildWhiteList();

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

    /**
     * ホワイトリストで定義されたタグは安全と見なし、それ以外は削除
     *
     * @param original_contents
     * @return
     */
    public String nomalizeFeedContents(String original_contents) {
        Document dirty = Jsoup.parse(original_contents);
        Cleaner cleaner = new Cleaner(WHITELIST);
        Document clean = cleaner.clean(dirty);
        Element body = clean.body();
        return body.html();
    }

    /**
     * ホワイトリスト定義
     *
     * @return
     */
    private static Whitelist buildWhiteList() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt", "em", "h1",
                "h2", "h3", "h4", "h5", "h6", "i", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub", "sup",
                "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul", "img");

        whitelist.addAttributes("div", "dir");
        whitelist.addAttributes("pre", "dir");
        whitelist.addAttributes("code", "dir");
        whitelist.addAttributes("table", "dir");
        whitelist.addAttributes("p", "dir");
        whitelist.addAttributes("a", "href", "title");
        whitelist.addAttributes("blockquote", "cite");
        whitelist.addAttributes("col", "span", "width");
        whitelist.addAttributes("colgroup", "span", "width");
        whitelist.addAttributes("img", "align", "alt", "height", "src", "title", "width", "style");
        whitelist.addAttributes("ol", "start", "type");
        whitelist.addAttributes("q", "cite");
        whitelist.addAttributes("table", "border", "bordercolor", "summary", "width");
        whitelist.addAttributes("td", "border", "bordercolor", "abbr", "axis", "colspan", "rowspan", "width");
        whitelist.addAttributes("th", "border", "bordercolor", "abbr", "axis", "colspan", "rowspan", "scope", "width");
        whitelist.addAttributes("ul", "type");

        whitelist.addProtocols("a", "href", "ftp", "http", "https", "mailto");
        whitelist.addProtocols("blockquote", "cite", "http", "https");
        whitelist.addProtocols("img", "src", "http", "https");
        whitelist.addProtocols("q", "cite", "http", "https");

        whitelist.addEnforcedAttribute("a", "target", "_blank");
        return whitelist;
    }
}
