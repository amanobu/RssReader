/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ama.rssreader.login.cdis;

import ama.rssreader.login.ejbs.UserRegistManager;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author amanobu
 */
@Named(value = "removePage")
@RequestScoped
public class RemovePage implements Serializable {
    private String userid;
    
    @EJB
    UserRegistManager userManager;

    /**
     * Creates a new instance of RemovePage
     */
    public RemovePage() {
    }
    
    public String removeDB(){
        userManager.removeUser(getUserid());
        return "rem-success";
    }

    /**
     * @return the username
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the username to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
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
