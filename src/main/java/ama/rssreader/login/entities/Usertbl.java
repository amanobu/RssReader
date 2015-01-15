/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ama.rssreader.login.entities;

import ama.rssreader.entities.Categorytbl;
import ama.rssreader.entities.Feedtbl;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author amanobu
 */
@Entity
@Table(name = "USERTBL")
@NamedQueries({
    @NamedQuery(name = "Usertbl.findAll", query = "SELECT u FROM Usertbl u"),
    @NamedQuery(name = "Usertbl.findByUserid", query = "SELECT u FROM Usertbl u WHERE u.userid = :userid"),
    @NamedQuery(name = "Usertbl.findByPw", query = "SELECT u FROM Usertbl u WHERE u.pw = :pw"),
    @NamedQuery(name = "Usertbl.findByRegdate", query = "SELECT u FROM Usertbl u WHERE u.regdate = :regdate"),
    @NamedQuery(name = "Usertbl.findByUpddate", query = "SELECT u FROM Usertbl u WHERE u.upddate = :upddate")})
public class Usertbl implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Categorytbl> categorytblCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid")
    private Collection<Feedtbl> feedtblCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "USERID")
    private String userid;
    @Size(max = 256)
    @Column(name = "PW")
    private String pw;
    @Column(name = "REGDATE")
    @Temporal(TemporalType.DATE)
    private Date regdate;
    @Column(name = "UPDDATE")
    @Temporal(TemporalType.DATE)
    private Date upddate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usertbl")
    private Collection<Grouptbl> grouptblCollection;

    public Usertbl() {
    }

    public Usertbl(String userid) {
        this.userid = userid;
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

    public Collection<Grouptbl> getGrouptblCollection() {
        return grouptblCollection;
    }

    public void setGrouptblCollection(Collection<Grouptbl> grouptblCollection) {
        this.grouptblCollection = grouptblCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usertbl)) {
            return false;
        }
        Usertbl other = (Usertbl) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ama.rssreader.Usertbl[ userid=" + userid + " ]";
    }

    public Collection<Categorytbl> getCategorytblCollection() {
        return categorytblCollection;
    }

    public void setCategorytblCollection(Collection<Categorytbl> categorytblCollection) {
        this.categorytblCollection = categorytblCollection;
    }

    public Collection<Feedtbl> getFeedtblCollection() {
        return feedtblCollection;
    }

    public void setFeedtblCollection(Collection<Feedtbl> feedtblCollection) {
        this.feedtblCollection = feedtblCollection;
    }
    
}
