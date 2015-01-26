package ama.rssreader.login.cdis;

import ama.rssreader.login.ejbs.UserRegistManager;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * ユーザ削除ページのバッキングビーンです
 *
 * @author amanobu
 */
@Named(value = "removePage")
@ViewScoped
public class RemovePage implements Serializable {

    @EJB
    UserRegistManager userManager;

    /**
     * Creates a new instance of RemovePage
     */
    public RemovePage() {
    }

    public void removeDB() {
        Logger.getLogger(RemovePage.class.getName()).log(Level.SEVERE, "RemovePage removeDB called", "");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        econtext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        String user = request.getRemoteUser();
        try {
            request.logout();
            userManager.removeUser(user);
        } catch (ServletException ex) {
            Logger.getLogger(RemovePage.class.getName()).log(Level.SEVERE, "Logout Fail" + ex.toString(), ex);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "削除失敗しました", user);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "削除完了しました",user);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * @return the username
     */
    public String getUserid() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        return request.getRemoteUser();
    }

    /**
     * @return the userManager
     */
    public UserRegistManager getUserManager() {
        return userManager;
    }

    /**
     * @param userManager the userManager to set
     */
    public void setUserManager(UserRegistManager userManager) {
        this.userManager = userManager;
    }

}
