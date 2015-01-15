package ama.rssreader.login.cdis;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named(value = "indexPage")
@RequestScoped
public class IndexPage implements Serializable{

    private String userid;
    private String pw;

    /**
     * Creates a new instance of IndexPage
     */
    public IndexPage() {
    }

    public String login() {
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"login called","");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        try {
            request.login(getUserid(), getPw());
            return "/reader/FeedList.xhtml?faces-redirect=true";
        } catch (ServletException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"ログインに失敗しました","ユーザ名・パスワードを確認ください"));
            return "";
        }
    }
    
    public String logout(){
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"logout called","");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        econtext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        try{
            request.logout();
        }catch (ServletException ex) {
            Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"Logout Fail"+ex.toString(),ex);
            return "";
        }
        return "FeedList.xhtml?faces-redirect=true";
    }
    
    // if logined,redirect to home.xhtml
    public void onPageLoad() throws ServletException{
        Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"onPageLoad called","");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext econtext = context.getExternalContext();
        //econtext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
        
        Principal p = request.getUserPrincipal();
        if(null != p){
            try{
                StringBuilder sb = new StringBuilder(request.getContextPath());
                
                sb.append("/app/login/home.xhtml");
                Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"login skip:"+sb.toString(),sb.toString());
                FacesContext.getCurrentInstance().getExternalContext().redirect(sb.toString());
            }catch(IOException e){
                Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"LoginSkip IOException",e);
                request.logout();
            }
        }else{
            Logger.getLogger(IndexPage.class.getName()).log(Level.SEVERE,"LoginSkip fail Principal is null","");
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
