package ama.rssreader.login.ejbs;

import ama.rssreader.login.entities.Grouptbl;
import ama.rssreader.login.entities.GrouptblPK;
import ama.rssreader.login.entities.Usertbl;
import ama.rssreader.login.util.SHA256Encoder;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * ユーザ管理用のクラスです
 * @author amanobu
 */
@Stateless
public class UserRegistManager {

    @PersistenceContext
    EntityManager em;
    
    /**
     * ユーザとグループを設定します
     * @param userid
     * @param password
     * @param group 
     */
    public void createUserAndGroup(String userid, String password, String group) {
        Usertbl user = new Usertbl();
        user.setUserid(userid);
        user.setPw(SHA256Encoder.encode(password));
        Date sysdate = new Date();
        user.setRegdate(sysdate);
        
        Grouptbl _group = new Grouptbl();
        _group.setGrouptblPK(new GrouptblPK(userid,group));
        _group.setUsertbl(user);
        _group.setRegdate(sysdate);
        
        em.persist(user);
        em.persist(_group);
        
    }
    public void removeUser(String userid){
        Usertbl user = em.find(Usertbl.class, userid);
        em.remove(user);
    }
    
    public Usertbl findUser(String userid){
        Usertbl user = em.find(Usertbl.class, userid);
        return user;
    }
    
    public void setLogindate(String userid){
        Usertbl user = em.find(Usertbl.class, userid);
        user.setUpddate(new Date());
    }
}
