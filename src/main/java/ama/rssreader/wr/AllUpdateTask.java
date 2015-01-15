package ama.rssreader.wr;

import ama.rssreader.cdis.RssReaderLogicCDI;
import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Contentstbl;
import ama.rssreader.entities.Feedtbl;
import ama.rssreader.util.LogUtil;
import ama.rssreader.util.RssUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 * 記事全更新を非同期で行うtaskです
 *
 * @author amanobu
 */
public class AllUpdateTask implements Runnable {

    @EJB
    RssReaderLogic rsslogic;

    /*コレがDIされないのはなぜだろう->RssReaderLogicCDIが@RequestScopedが原因の様だ*/
    @Inject
    RssReaderLogicCDI rsslogicCDI;

    public AllUpdateTask() {
        LogUtil.log(AllUpdateTask.class.getName(), Level.INFO, "AllUpdateTask constructor");
    }

    @Override
    public void run() {

        try {
            updateAllFeed();
            /*CDI BeanがDIされないので・・・・コードが重複する.なぜEJBはDIされるのだろう・・・*/
            //rsslogicCDI.updateAllFeed();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登録されているFeed全件の記事取得を実行します バッチで定期的に呼び出される事を目的
     */
    public void updateAllFeed() {
        LogUtil.log(RssReaderLogic.class.getName(), Level.INFO, "全件Feed更新が呼ばれました");
        List<Integer> list = rsslogic.getAllRssId();
        for (Integer rssid : list) {
            try {
                LogUtil.log(RssReaderLogic.class.getName(), Level.INFO, "全件更新対象RSSID:", rssid);
                updateFeed(rssid);
            } catch (Exception e) {
                LogUtil.log(RssReaderLogic.class.getName(), Level.INFO, "アップデート失敗 RSSID:", rssid);
            }
        }
    }

    /**
     * 記事の取得 登録RSS IDのURLから未登録の記事を追加します 記事URLでユニークの判断をしています
     *
     * @param rssid
     * @throws Exception
     */
    public void updateFeed(Integer rssid) throws Exception {
        LogUtil.log(SUBLOGSTR, Level.ALL, "更新対象のRSSID", rssid);
        Feedtbl feed = rsslogic.getFeed(rssid);
        String url = feed.getUrl();
        List<Contentstbl> list = RssUtil.getFeeds(url);
        LogUtil.log(SUBLOGSTR, Level.ALL, "取得した記事一覧:" + list.toString());
        //特に指定しない限りCMT(コンテナ管理トランザクション)のため、明示的なトランザクションはダメと言われる様だ
        //em.getTransaction().begin();
        Date now = new Date();
        for (Contentstbl contents : list) {
            contents.setReadflg(false);
            contents.setRssid(new Feedtbl(rssid));
            contents.setRegdate(now);
            LogUtil.log(SUBLOGSTR, Level.ALL, "登録する記事", contents.getTitle());
            try {
                rsslogic.registContents(contents);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.log(SUBLOGSTR, Level.ALL, "登録失敗した記事Exception:", contents.getTitle(), e.getLocalizedMessage());
            }
        }
        //em.getTransaction().commit(); 
    }
    private static final String SUBLOGSTR = AllUpdateTask.class.getName();
}
