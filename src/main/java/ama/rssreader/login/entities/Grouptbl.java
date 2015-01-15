/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ama.rssreader.login.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author amanobu
 */
@Entity
@Table(name = "GROUPTBL")
@NamedQueries({
    @NamedQuery(name = "Grouptbl.findAll", query = "SELECT g FROM Grouptbl g"),
    @NamedQuery(name = "Grouptbl.findByUserid", query = "SELECT g FROM Grouptbl g WHERE g.grouptblPK.userid = :userid"),
    @NamedQuery(name = "Grouptbl.findByGroupid", query = "SELECT g FROM Grouptbl g WHERE g.grouptblPK.groupid = :groupid"),
    @NamedQuery(name = "Grouptbl.findByRegdate", query = "SELECT g FROM Grouptbl g WHERE g.regdate = :regdate"),
    @NamedQuery(name = "Grouptbl.findByUpddate", query = "SELECT g FROM Grouptbl g WHERE g.upddate = :upddate")})
public class Grouptbl implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GrouptblPK grouptblPK;
    @Column(name = "REGDATE")
    @Temporal(TemporalType.DATE)
    private Date regdate;
    @Column(name = "UPDDATE")
    @Temporal(TemporalType.DATE)
    private Date upddate;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usertbl usertbl;

    public Grouptbl() {
    }

    public Grouptbl(GrouptblPK grouptblPK) {
        this.grouptblPK = grouptblPK;
    }

    public Grouptbl(String userid, String groupid) {
        this.grouptblPK = new GrouptblPK(userid, groupid);
    }

    public GrouptblPK getGrouptblPK() {
        return grouptblPK;
    }

    public void setGrouptblPK(GrouptblPK grouptblPK) {
        this.grouptblPK = grouptblPK;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Date getUpddate() {
        return upddate;
    }

    public void setUpddate(Date upddate) {
        this.upddate = upddate;
    }

    public Usertbl getUsertbl() {
        return usertbl;
    }

    public void setUsertbl(Usertbl usertbl) {
        this.usertbl = usertbl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grouptblPK != null ? grouptblPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grouptbl)) {
            return false;
        }
        Grouptbl other = (Grouptbl) object;
        if ((this.grouptblPK == null && other.grouptblPK != null) || (this.grouptblPK != null && !this.grouptblPK.equals(other.grouptblPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ama.rssreader.Grouptbl[ grouptblPK=" + grouptblPK + " ]";
    }
    
}
