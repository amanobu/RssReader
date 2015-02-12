package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Contentstbl;
import ama.rssreader.entities.Feedtbl;
import ama.rssreader.util.LogUtil;
import ama.rssreader.util.RssUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * CMT(コンテナ管理トランザクション)だとEJBのメソッド毎のトランザクション管理だと問題があるロジックを記載するクラス
 * @ApplicationScopedにした理由はバッチ起動にもInject出来る様にする為
 * @author ama_nobu
 */
@Named(value = "RssReaderLogicCDI")
@ApplicationScoped
public class RssReaderLogicCDI {

    private static final String SUBLOGSTR = RssReaderLogicCDI.class.getName();

    @EJB
    RssReaderLogic rsslogic;
    

    public RssReaderLogicCDI() {

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
        Date now = new Date();
        boolean successflg = false;
        for (Contentstbl contents : list) {
            contents.setReadflg(false);
            contents.setRssid(new Feedtbl(rssid));
            contents.setRegdate(now);
            LogUtil.log(SUBLOGSTR, Level.ALL, "登録する記事", contents.getTitle());
            try {
                if(rsslogic.registContents(contents)){
                    successflg = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.log(SUBLOGSTR, Level.ALL, "登録失敗した記事Exception:", contents.getTitle(), e.getLocalizedMessage());
            }
        }
        if(successflg){
            //一件でも更新した物があったのでFeedtblのupdateを更新する
            feed.setUpddate(new Date());
            rsslogic.updateFeed(feed);
        }
    }
}
