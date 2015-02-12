package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Contentstbl;
import ama.rssreader.util.LogUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author amanobu
 */
@Named(value = "contentsListPage")
@RequestScoped
public class ContentsListPage implements Serializable {

    @EJB
    RssReaderLogic rsslogic;

    @Inject
    ConvsastionBean bean;
    /**
     * 選択されたrssid
     */
    private int rssid;

    public int getRssid() {
        return rssid;
    }

    public void setRssid(int rssid) {
        this.rssid = rssid;
    }

    public ContentsListPage() {
    }
    /*  画面間のパラメータ私はFlashを使うことらしいが、SessionScopeの最低限のBeanをやりくりしたほうがよさそうなのでこうした
    
     @PostConstruct
     public void load() {
     FacesContext context = FacesContext.getCurrentInstance();
     ExternalContext econtext = context.getExternalContext();
     setRssid((Integer) econtext.getFlash().get("rssid"));
     LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "load called:", getRssid());
     }
     */

    public void updateReadFlg(Integer contentsid, boolean flag) {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String userid = request.getRemoteUser();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "updateReadFlg called:", request.getRemoteUser(), "flag:", flag);
        rsslogic.updateReadflg(userid, contentsid, flag);
        updateView();
    }

    public void getContentsList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        //setRssid((Integer) econtext.getFlash().get("rssid"));
        setRssid(bean.getSelectedRssid());
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "getContentsList called:", request.getRemoteUser(), "ConvsastionBean->", bean.toString());
        String user = request.getRemoteUser();
        bean.setSelectedRssid(getRssid());
        List<Contentstbl> list = rsslogic.getContentsList(rssid, user);
        //LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "getContentsList called:", request.getRemoteUser(), "ConvsastionBean->", bean.toString(),"list",list.toString());
        setList(list);
    }

    public void getUnreadContentsList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        //setRssid((Integer) econtext.getFlash().get("rssid"));
        setRssid(bean.getSelectedRssid());
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "getUnreadContentsList called:", request.getRemoteUser(), "ConvsastionBean->", bean.toString());
        String user = request.getRemoteUser();
        bean.setSelectedRssid(getRssid());
        setList(rsslogic.getUnreadContentsList(rssid, user));
    }

    public List<Contentstbl> getList() {
        return list;
    }

    public void setList(List<Contentstbl> list) {
        this.list = list;
    }

    private List<Contentstbl> list;

    @PostConstruct
    public void updateView() {
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "updateView called:", bean.isSelectedAllView(), "ConvsastionBean->", bean.toString());
        if (bean.isSelectedAllView()) {
            getContentsList();
        } else {
            getUnreadContentsList();
        }
    }

    public void setViewFlg(boolean flg) {
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "setViewFlg called:", flg, "ConvsastionBean->", bean.toString());
        bean.setSelectedAllView(flg);
        updateView();
    }

    /**
     * 既読未読フラグを全更新します
     *
     * @param flag
     */
    public void updateAllReadFlag(boolean flag) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String userid = request.getRemoteUser();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "updateAllReadFlag called:", "rssid:", bean.getSelectedRssid(), "flag:", flag);
        rsslogic.updataAllReadflg(userid, bean.getSelectedRssid(), flag);
        updateView();
    }
}
