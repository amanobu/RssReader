/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ama.rssreader.entities;

import ama.rssreader.login.entities.Usertbl;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CATEGORYTBL")
@NamedQueries({
    @NamedQuery(name = "Categorytbl.findAll", query = "SELECT c FROM Categorytbl c"),
    @NamedQuery(name = "Categorytbl.findByCategoryid", query = "SELECT c FROM Categorytbl c WHERE c.categoryid = :categoryid"),
    @NamedQuery(name = "Categorytbl.findByCategoryname", query = "SELECT c FROM Categorytbl c WHERE c.categoryname = :categoryname"),
    @NamedQuery(name = "Categorytbl.findByRegdate", query = "SELECT c FROM Categorytbl c WHERE c.regdate = :regdate"),
    @NamedQuery(name = "Categorytbl.findByUpddata", query = "SELECT c FROM Categorytbl c WHERE c.upddata = :upddata")})
public class Categorytbl implements Serializable {
    @OneToMany(mappedBy = "categoryid")
    private Collection<Feedtbl> feedtblCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CATEGORYID")
    private Integer categoryid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "CATEGORYNAME")
    private String categoryname;
    @Column(name = "REGDATE")
    @Temporal(TemporalType.DATE)
    private Date regdate;
    @Column(name = "UPDDATA")
    @Temporal(TemporalType.DATE)
    private Date upddata;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private Usertbl userid;

    public Categorytbl() {
    }

    public Categorytbl(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Categorytbl(Integer categoryid, String categoryname) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Date getUpddata() {
        return upddata;
    }

    public void setUpddata(Date upddata) {
        this.upddata = upddata;
    }

    public Usertbl getUserid() {
        return userid;
    }

    public void setUserid(Usertbl userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryid != null ? categoryid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorytbl)) {
            return false;
        }
        Categorytbl other = (Categorytbl) object;
        if ((this.categoryid == null && other.categoryid != null) || (this.categoryid != null && !this.categoryid.equals(other.categoryid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ama.rssreader.entities.Categorytbl[ categoryid=" + categoryid + " ]";
    }

    public Collection<Feedtbl> getFeedtblCollection() {
        return feedtblCollection;
    }

    public void setFeedtblCollection(Collection<Feedtbl> feedtblCollection) {
        this.feedtblCollection = feedtblCollection;
    }
    
}
