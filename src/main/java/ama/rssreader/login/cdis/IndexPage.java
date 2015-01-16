package ama.rssreader.login.cdis;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named(value = "indexPage")
@ViewScoped
public class IndexPage implements Serializable {

    private String userid;
    private String pw;

    /**
     * Creates a new instance of IndexPage
     */
    public IndexPage() {
    }

    /**
     * ログイン後要求されたページにリダイレクトする為の保存場所
     * http://stackoverflow.com/questions/2206911/performing-user-authentication-in-java-ee-jsf-using-j-security-check
     * http://stackoverflow.com/questions/13420748/java-ee-security-not-redirected-to-initial-page-after-login
     */
    private String originalURL;

    @PostConstruct
    public void init() {
        /*
        http://stackoverflow.com/questions/2206911/performing-user-authentication-in-java-ee-jsf-using-j-security-check
        の内容をコピペ
        */
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/home.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "login init:" + originalURL, "");
    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        String originalURI = (String) econtext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "login called:" + originalURI, "");
        try {
            request.login(getUserid(), getPw());
            //保存して置いた呼び出し先にリダイレクトする
            econtext.redirect(originalURL);
            //return "/reader/FeedList.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ログインに失敗しました", "ユーザ名・パスワードを確認ください"));
            //return "";
        }
    }

    public String logout() {
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "logout called", "");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        econtext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        try {
            request.logout();
        } catch (ServletException ex) {
            Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "Logout Fail" + ex.toString(), ex);
            return "";
        }
        return "FeedList.xhtml?faces-redirect=true";
    }

    // if logined,redirect to home.xhtml
    public void onPageLoad() throws ServletException {
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "onPageLoad called", "");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        //econtext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();

        Principal p = request.getUserPrincipal();
        if (null != p) {
            try {
                StringBuilder sb = new StringBuilder(request.getContextPath());

                sb.append("/app/login/home.xhtml");
                Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "login skip:" + sb.toString(), sb.toString());
                FacesContext.getCurrentInstance().getExternalContext().redirect(sb.toString());
            } catch (IOException e) {
                Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "LoginSkip IOException", e);
                request.logout();
            }
        } else {
            Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE, "LoginSkip fail Principal is null", "");
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

}
