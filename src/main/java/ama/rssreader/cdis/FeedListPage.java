package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Feedtbl;
import ama.rssreader.util.LogUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

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

    /**
     * Creates a new instance of FeedListPage
     */
    public FeedListPage() {
    }

    public List<Feedtbl> getFeedList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "getFeedList called:", request.getRemoteUser());

        return rsslogic.feedList(request.getRemoteUser());
    }

    public void feedUpdate(Integer rssid) {
        try {
            rsslogiccdi.updateFeed(rssid);
        } catch (Exception e) {
            LogUtil.log(FeedListPage.class.getName(), Level.INFO, "feedUpdate catchExcepton", e.getLocalizedMessage());
        }
    }

    private int rssid;

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
    }

    private MenuModel model;

    public void updateMenuModel() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String user = request.getRemoteUser();
        MenuModel newModel = new DefaultMenuModel();
        List<Feedtbl> feedlist = getFeedList();
        Set<String> categoryset = new HashSet();
        for (Feedtbl feedtbl : feedlist) {
            String strCategory = feedtbl.getCategoryid().getCategoryname();
            if(!categoryset.contains(strCategory)){
                categoryset.add(strCategory);
            }
        }
        Map<String,DefaultSubMenu> defaultSubMenuMap = new HashMap<>();
        for (String categoryname : categoryset){
            defaultSubMenuMap.put(categoryname, new DefaultSubMenu(categoryname));
        }
        
        for (Feedtbl feedtbl : feedlist) {
            String strCategory = feedtbl.getCategoryid().getCategoryname();
            DefaultSubMenu submenu = defaultSubMenuMap.get(strCategory);
            DefaultMenuItem item = new DefaultMenuItem(feedtbl.getTitle());
            item.setOnclick(feedtbl.getTitle());
            submenu.addElement(item);
        }
        
        for (DefaultSubMenu menu: defaultSubMenuMap.values()){
            model.addElement(menu);
        }
    }
}
