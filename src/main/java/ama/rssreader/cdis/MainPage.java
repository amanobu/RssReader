package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.entities.Contentstbl;
import ama.rssreader.entities.Feedtbl;
import ama.rssreader.util.LogUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
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
@Named(value = "mainPage")
@RequestScoped
@Data
public class MainPage {

    public MainPage() {
        this.selectedAllView = false;
    }

    @EJB
    RssReaderLogic rsslogic;

    @Inject
    RssReaderLogicCDI rsslogiccdi;

    //表示中のrssidの格納
    private int rssid;

    private List<Feedtbl> feeds;

    private List<Contentstbl> list;

    //true:全件表示 false:未読表示
    boolean selectedAllView;

    @PostConstruct
    public void getFeedList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "getFeedList called:", request.getRemoteUser());

        setFeeds(rsslogic.feedList(request.getRemoteUser()));
        updateMenuModel();
    }

    public void feedUpdate(Integer rssid) {
        try {
            rsslogiccdi.updateFeed(rssid);
        } catch (Exception e) {
            LogUtil.log(FeedListPage.class.getName(), Level.INFO, "feedUpdate catchExcepton", e.getLocalizedMessage());
        }
    }

    private long getUnreadCount(Integer rssid) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        long count = rsslogic.getUnreadCount(request.getRemoteUser(), rssid);
        LogUtil.log(FeedListPage.class.getName(), Level.INFO, "getUnreadCount called", request.getRemoteUser(), "count:", Long.toString(count));
        return count;
    }

    private MenuModel model;

    public void updateMenuModel() {
        //以下の様な構造になるみたい
        //DefaultMenuModel
        // - DefaultSubMenu
        //    - DefaultMenuItem
        //    - DefaultMenuItem
        // - DefaultSubMenu
        //    - DefaultMenuItem
        //    - DefaultMenuItem

        //再構築するROOT
        MenuModel newModel = new DefaultMenuModel();

        List<Feedtbl> feedlist = getFeeds();
        Set<String> categoryset = new HashSet();

        //全カテゴリの抽出
        for (Feedtbl feedtbl : feedlist) {
            if (null == feedtbl.getCategoryid()) {
                continue;
            }
            String strCategory = feedtbl.getCategoryid().getCategoryname();
            if (!categoryset.contains(strCategory)) {
                categoryset.add(strCategory);
            }
        }

        Map<String, DefaultSubMenu> defaultSubMenuMap = new HashMap<>();

        //カテゴリが設定されていない物をALLのカテゴリに入れる
        defaultSubMenuMap.put("ALL", new DefaultSubMenu("ALL"));

        //全カテゴリをDefaultMenuModelにDefaultSubMenuとして追加する
        for (String categoryname : categoryset) {
            defaultSubMenuMap.put(categoryname, new DefaultSubMenu(categoryname));
        }

        //DefaultSubMenuの下にDefaultMenuItemを入れる
        for (Feedtbl feedtbl : feedlist) {
            DefaultSubMenu submenu;
            if (null == feedtbl.getCategoryid()) {
                //カテゴリが設定されていない場合
                submenu = defaultSubMenuMap.get("ALL");
            } else {
                String strCategory = feedtbl.getCategoryid().getCategoryname();
                //カテゴリが設定されている場合
                submenu = defaultSubMenuMap.get(strCategory);
            }
            DefaultMenuItem item = new DefaultMenuItem(feedtbl.getTitle());
            item.setValue(feedtbl.getTitle());
            //item.setCommand("#{mainPage.setRssid(" + feedtbl.getRssid().toString() + ")}");

            item.setCommand("#{mainPage.getContentsList(" + feedtbl.getRssid().toString() + ")}");
            item.setUpdate("contentslist");
            //ここでjsを呼び出しても、上のcontentslistがアップデートされないうちに実行されるのでjsがわで変更を検知(DOMSubtreeModified)するようにした
            //item.setOnsuccess("test()");

//            item.setOnsuccess(";console.log(\'aaa\')");
            if (getUnreadCount(feedtbl.getRssid().intValue()) > 0) {
                item.setStyle("myclass_unread");
            }
            submenu.addElement(item);
        }

        for (DefaultSubMenu menu : defaultSubMenuMap.values()) {
            newModel.addElement(menu);
        }
        setModel(newModel);
    }

    public void getContentsList(Integer rssid) {
        this.setRssid(rssid);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        //setRssid((Integer) econtext.getFlash().get("rssid"));
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String user = request.getRemoteUser();
        List<Contentstbl> list = rsslogic.getContentsList(rssid, user);
        //LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "getContentsList called:", request.getRemoteUser(), "ConvsastionBean->", bean.toString(), "list", list.toString());
        setList(list);
    }

    public void updateReadFlg(Integer contentsid, boolean flag) {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String userid = request.getRemoteUser();
        LogUtil.log(ContentsListPage.class.getName(), Level.INFO, "updateReadFlg called:", request.getRemoteUser(), "flag:", flag);
        rsslogic.updateReadflg(userid, contentsid, flag);
        updateView();
    }

    public void updateView() {
        if (selectedAllView) {
            getContentsList(rssid);
        } else {
            getUnreadContentsList();
        }
    }

    public void getUnreadContentsList() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String user = request.getRemoteUser();
        setList(rsslogic.getUnreadContentsList(rssid, user));
    }
}
