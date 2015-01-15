package ama.rssreader.login.cdis;

import ama.rssreader.login.ejbs.RoleCheckLogic;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "homePage")
@RequestScoped
public class HomePage implements Serializable{

    /**
     * Creates a new instance of HomePage
     */
    public HomePage() {
    }
    
    public boolean isUserInRole(String role){
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role);
    }
    
    @EJB
    RoleCheckLogic roleCheckLogic;
    
    private String roleCheckString;
    
    public String getRoleCheckerString(){
        if(isUserInRole("admin")){
            String adminRoleString = roleCheckLogic.executableByAdmin();
            roleCheckString = adminRoleString;
        }
        if(isUserInRole("user")){
            String userRoleString = roleCheckLogic.executableByUser();
            roleCheckString = userRoleString;
        }
        return roleCheckString;
    }
}
