package ama.rssreader.login.cdis;

import ama.rssreader.login.ejbs.UserRegistManager;
import ama.rssreader.login.entities.Usertbl;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;

@Named(value = "registPage")
@RequestScoped
public class RegistPage implements Serializable {
    
    @Size(min = 3, message = "3文字以上128文字以内で入力してください")
    private String userid;
    //コレは適当。DBのカラムからしてどこまで入るかわからない
    @Size(min = 5, message = "5文字以上で入力してください")
    private String pw;
    
    @EJB
    UserRegistManager userRegist;

    /**
     * group名は固定でuser
     */
    private static final String group = "user";
    
    public String registDB() {
        //既存ユーザがいないか確認
        Usertbl user = userRegist.findUser(getUserid());
        if (null == user) {
            //同名のuserがいなければ登録
            userRegist.createUserAndGroup(getUserid(), getPw(), group);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "登録成功しました", ""));
            //登録完了後ユーザ名が入ったままになるのでリセットしている
            setUserid("");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "登録出来ませんでした", "すでに同名のユーザがあります"));
        }
        return "";
    }

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
