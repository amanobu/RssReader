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
        LogUtil.log(SUBLOGSTR, Level.INFO, "AllUpdateTask constructor");
    }

    @Override
    public void run() {
        try {
            rsslogicCDI.updateAllFeed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final String SUBLOGSTR = AllUpdateTask.class.getName();
}
