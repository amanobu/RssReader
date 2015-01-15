package ama.rssreader.ejbs;

import ama.rssreader.entities.Contentstbl;
import ama.rssreader.entities.Feedtbl;
import ama.rssreader.login.entities.Usertbl;
import ama.rssreader.util.LogUtil;
import ama.rssreader.util.RssUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author amanobu
 */
@Stateless
public class RssReaderLogic {

    private static final String SUBLOGSTR = RssReaderLogic.class.getName();

    @PersistenceContext
    EntityManager em;

    /**
     * 新規Feedの登録
     *
     * @param userid
     * @param url
     * @throws Exception
     */
    public void regFeed(String userid, String url) throws Exception {
        String title = RssUtil.getTitle(url);
        Feedtbl feed = new Feedtbl();
        feed.setTitle(title);
        feed.setUrl(url);
        Usertbl user = new Usertbl();
        user.setUserid(userid);
        feed.setUserid(user);
        feed.setRegdate(new Date());

        em.persist(feed);
    }

    /**
     * Feedの削除
     *
     * @param userid
     * @param rssid
     */
    public void delFeed(String userid, Integer rssid) {
        LogUtil.log(RssReaderLogic.class.getName(), Level.INFO, "delFeed called:", userid, "rssid", rssid);
        Feedtbl feed = em.find(Feedtbl.class, rssid);
        em.remove(feed);
    }

    /**
     * 登録されているFeed一覧を取得します
     *
     * @param userid 取得する一覧の利用者ID
     * @return
     */
    public List<Feedtbl> feedList(String userid) {
        Logger.getLogger(RssReaderLogic.class.getName()).log(Level.SEVERE, "RssReaderLogic feedList called:" + userid, "");
        Query q = em.createQuery("select object(o) from Feedtbl as o WHERE o.userid = :userid", Feedtbl.class);
        q.setParameter("userid", new Usertbl(userid));
        return q.getResultList();
    }

    /**
     * 登録されているFeedの詳細を取得します
     *
     * @param rssid
     * @return
     */
    public Feedtbl getFeed(Integer rssid) {
        //Logger.getLogger(RssReaderLogic.class.getName()).log(Level.SEVERE, "RssReaderLogic getFeed called:" + rssid, "");
        Feedtbl feed = (Feedtbl) em.createNamedQuery("Feedtbl.findByRssid").setParameter("rssid", rssid).getSingleResult();
        //Logger.getLogger(RssReaderLogic.class.getName()).log(Level.SEVERE, "RssReaderLogic getFeed called:" + feed.getTitle() + "," + feed.getUrl(), "");

        return feed;
    }


    
    public void registContents(Contentstbl contents){
        em.persist(contents);
    }

    /**
     * 記事一覧の取得
     *
     * @param rssid 登録RSS ID
     * @param userid 利用者ID
     * @return 記事の一覧
     */
    public List<Contentstbl> getContentsList(Integer rssid, String userid) {
        if (!check(rssid, userid)) {
            return new ArrayList();
        }
        Query q = em.createQuery("select object(o) from Contentstbl as o WHERE o.rssid = :rssid order by o.publishdate desc", Contentstbl.class);
        q.setParameter("rssid", new Feedtbl(rssid));

        return q.getResultList();
    }

    /**
     * 未読の記事数のカウント
     *
     * @param userid
     * @param rssid
     * @return
     */
    public long getUnreadCount(String userid, Integer rssid) {
        if (!check(rssid, userid)) {
            return 0;
        } else {
            Query q = em.createQuery("select count(o) from Contentstbl as o WHERE o.readflg = false and o.rssid = :rssid", Contentstbl.class);
            q.setParameter("rssid", new Feedtbl(rssid));
            return (long) q.getSingleResult();
        }
    }

    /**
     * 記事の既読、未読フラグの更新
     *
     * @param userid
     * @param contentsid
     * @param flag false:未読 true:既読
     */
    public void updateReadflg(String userid, Integer contentsid, boolean flag) {
        Contentstbl contents = em.find(Contentstbl.class, contentsid);
        Integer rssid = contents.getRssid().getRssid();
        if (check(rssid, userid)) {
            contents.setReadflg(flag);
            //以下
            //em.getTransaction().commit();
            //のように書くと
            //javax.servlet.ServletException: java.lang.IllegalStateException: Exception Description: Cannot use an EntityTransaction while using JTA.
            //となる。なので、こう書いた。JTAがトランザクション管理をしているため。もし自分でとなるとpersistence.xmlでNON-JTA-DATA-SOURCEを指定するか、あるいは、UserTransactionによってコンテナベースのトランザクションを用いるか選択する必要があるそうだ
            em.persist(contents);
        }
    }

    /**
     * 該当の記事全件をflagの値に更新する
     *
     * @param userid
     * @param contentsid
     * @param flag
     */
    public void updataAllReadflg(String userid, Integer rssid, boolean flag) {
        if (check(rssid, userid)) {
            Query q = em.createQuery("update Contentstbl o set o.readflg = :flag where o.rssid = :rssid");
            q.setParameter("flag", flag);
            q.setParameter("rssid", new Feedtbl(rssid));
            q.executeUpdate();
        }
    }

    /**
     * 処理対処のデータが認証者の物か確認
     *
     * @param rssid
     * @param userid
     * @return
     */
    private boolean check(Integer rssid, String userid) {
        Feedtbl feed = getFeed(rssid);
        if (!feed.getUserid().getUserid().equals(userid)) {
            Logger.getLogger(RssReaderLogic.class.getName()).log(Level.SEVERE, "不正なアクセス:" + rssid + "," + userid + "," + feed.getUserid(), "");
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 全件のRssIdを取得します
     *
     */
    public List<Integer> getAllRssId() {
        Query q = em.createNativeQuery("SELECT RSSID FROM FEEDTBL");
        List<Integer> list = q.getResultList();
        LogUtil.log(RssReaderLogic.class.getName(), Level.INFO, "全件rssidList:", list);
        return list;
    }
}
