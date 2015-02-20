package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Feedtbl;
import ama.rssreader.util.LogUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;

/**
 *
 * @author amanobu
 */
@Named(value = "feedListPage")
@RequestScoped
@Data
public class FeedListPage implements Serializable {

    @EJB
    RssReaderLogic rsslogic;

    @Inject
    RssReaderLogicCDI rsslogiccdi;

    @Inject
    ConvsastionBean bean;

    private int rssid;

    private List<Feedtbl> feeds;

    /**
     * Creates a new instance of FeedListPage
     */
    public FeedListPage() {
    }

    @PostConstruct
    public void getFeedList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "getFeedList called:", request.getRemoteUser());

        setFeeds(rsslogic.feedList(request.getRemoteUser()));
    }

    public void feedUpdate(Integer rssid) {
        try {
            rsslogiccdi.updateFeed(rssid);
        } catch (Exception e) {
            LogUtil.log(FeedListPage.class.getName(), Level.INFO, "feedUpdate catchExcepton", e.getLocalizedMessage());
        }
    }

    public int getRssid() {
        return rssid;
    }

    public void setRssid(int rssid) {
        this.rssid = rssid;
    }

    public String getContentsList(Integer rssid) {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();

        Flash flash = econtext.getFlash();
        //画面遷移用に選択されたrssidを設定→ConvsastionBeanでセッション間の情報を共有する
        //flash.put("rssid", rssid);
        bean.setSelectedRssid(rssid);
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "getContentsList called:", request.getRemoteUser(), "posted rssid:", rssid, "SessionInjectBean rssid", bean.getSelectedRssid());
        /*
         String user = request.getRemoteUser();
         setList(rsslogic.getContentsList(rssid, user));
         */
        return "ContentsList?faces-redirect=true";
    }

    public long getUnreadCount(Integer rssid) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        long count = rsslogic.getUnreadCount(request.getRemoteUser(), rssid);
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "getUnreadCount called", request.getRemoteUser(), "count:", Long.toString(count));
        return count;
    }

    public void deleteFeed(Integer rssid) {
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "deleteFeed called rssid:", rssid);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        rsslogic.delFeed(request.getRemoteUser(), rssid);
        //リストを更新したので最新に更新する
        getFeedList();
    }
}
