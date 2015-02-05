package ama.rssreader.cdis;

import ama.rssreader.util.LogUtil;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;

/**
 *
 * @author amanobu
 */
@Named(value = "covBean")
@SessionScoped
public class ConvsastionBean implements Serializable {
    /**
     * 記事一覧画面を表示する際に選択したRssID
     */
    private int selectedRssid;
    
    /**
     * 記事一覧画面で全件（既読を含め表示するときにこのフラグをtrueにする
     */
    private boolean selectedAllView;

    public boolean isSelectedAllView() {
        return selectedAllView;
    }

    public void setSelectedAllView(boolean selectedAllView) {
        this.selectedAllView = selectedAllView;
    }

    public int getSelectedRssid() {
        return selectedRssid;
    }

    public void setSelectedRssid(int selectedRssid) {
        this.selectedRssid = selectedRssid;
    }

    public int getSelectedContentsid() {
        return selectedContentsid;
    }

    public void setSelectedContentsid(int selectedContentsid) {
        this.selectedContentsid = selectedContentsid;
    }
    private int selectedContentsid;

    /**
     * Creates a new instance of CovBean
     */
    public ConvsastionBean() {
        LogUtil.log(this.getClass().getName(), Level.INFO, "ConvsastionBean initialized",selectedAllView);
        //初期表示時は未読で表示する為
        selectedAllView = false;
    }
    
    @Override
    public String toString(){
        return "selectedRssid:"+selectedRssid+",selectedAllView:"+selectedAllView;
    }
}
