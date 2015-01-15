package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Contentstbl;
import ama.rssreader.util.LogUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author amanobu
 */
@Named(value = "contentsListPage")
@ViewScoped
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
    }

    public List<Contentstbl> getContentsList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        //setRssid((Integer) econtext.getFlash().get("rssid"));
        setRssid(bean.getSelectedRssid());
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "getContentsList called:", request.getRemoteUser(), "ConvsastionBean->", bean.toString());
        String user = request.getRemoteUser();
        bean.setSelectedRssid(getRssid());
        return rsslogic.getContentsList(rssid, user);
    }

    private boolean selectedAllView;

    public boolean isSelectedAllView() {
        return selectedAllView;
    }

    public void setSelectedAllView(boolean selectedAllView) {
        this.selectedAllView = selectedAllView;
    }

    /*
     やりたいことが実現できないため、コメントアウト
     public void setSelectedAllViewFlag(boolean flag) {
        
     // ajaxで更新する場合@RequestScopedの場合一度状態が破棄されるため、flagの値が更新できない
     //@ViewScopedにすれば解決する
     //
     LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "setSelectedAllViewFlag called:", "flag:", flag, "bean:", bean.toString());
     this.selectedAllView = flag;
     bean.setSelectedAllView(flag);
     }
     */
    
    /**
     * 既読未読フラグを全更新します
     * @param flag 
     */
    public void updateAllReadFlag(boolean flag) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String userid = request.getRemoteUser();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "updateAllReadFlag called:","rssid:",bean.getSelectedRssid(),"flag:",flag);
        rsslogic.updataAllReadflg(userid, bean.getSelectedRssid(), flag);
    }
}
