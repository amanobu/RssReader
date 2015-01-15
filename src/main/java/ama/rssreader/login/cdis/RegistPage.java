package ama.rssreader.login.cdis;

import ama.rssreader.login.ejbs.UserRegistManager;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "registPage")
@RequestScoped
public class RegistPage implements Serializable{

    private String userid;
    private String pw;
    
    @EJB
    UserRegistManager userRegist;

    private static final String group = "user";
    public String registDB() throws IOException {
        userRegist.createUserAndGroup(getUserid(), getPw(), group);
        return "reg-success";
    }

    /**
     * Creates a new instance of RegistPage
     */
    public RegistPage() {
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
