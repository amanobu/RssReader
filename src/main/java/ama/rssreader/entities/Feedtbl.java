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
import javax.persistence.CascadeType;
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
@Table(name = "FEEDTBL")
@NamedQueries({
    @NamedQuery(name = "Feedtbl.findAll", query = "SELECT f FROM Feedtbl f"),
    @NamedQuery(name = "Feedtbl.findByRssid", query = "SELECT f FROM Feedtbl f WHERE f.rssid = :rssid"),
    @NamedQuery(name = "Feedtbl.findByTitle", query = "SELECT f FROM Feedtbl f WHERE f.title = :title"),
    @NamedQuery(name = "Feedtbl.findByUrl", query = "SELECT f FROM Feedtbl f WHERE f.url = :url"),
    @NamedQuery(name = "Feedtbl.findByDescription", query = "SELECT f FROM Feedtbl f WHERE f.description = :description"),
    @NamedQuery(name = "Feedtbl.findByRegdate", query = "SELECT f FROM Feedtbl f WHERE f.regdate = :regdate"),
    @NamedQuery(name = "Feedtbl.findByUpddate", query = "SELECT f FROM Feedtbl f WHERE f.upddate = :upddate")})
public class Feedtbl implements Serializable {
    @JoinColumn(name = "CATEGORYID", referencedColumnName = "CATEGORYID")
    @ManyToOne
    private Categorytbl categoryid;
    @Column(name = "UNREAD_COUNT")
    private Integer unreadCount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rssid")
    private Collection<Contentstbl> contentstblCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RSSID")
    private Integer rssid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "URL")
    private String url;
    @Size(max = 1024)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "REGDATE")
    @Temporal(TemporalType.DATE)
    private Date regdate;
    @Column(name = "UPDDATE")
    @Temporal(TemporalType.DATE)
    private Date upddate;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false)
    private Usertbl userid;

    public Feedtbl() {
    }

    public Feedtbl(Integer rssid) {
        this.rssid = rssid;
    }

    public Feedtbl(Integer rssid, String title, String url) {
        this.rssid = rssid;
        this.title = title;
        this.url = url;
    }

    public Integer getRssid() {
        return rssid;
    }

    public void setRssid(Integer rssid) {
        this.rssid = rssid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Usertbl getUserid() {
        return userid;
    }

    public void setUserid(Usertbl userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rssid != null ? rssid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feedtbl)) {
            return false;
        }
        Feedtbl other = (Feedtbl) object;
        if ((this.rssid == null && other.rssid != null) || (this.rssid != null && !this.rssid.equals(other.rssid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ama.rssreader.cdis.Feedtbl[ rssid=" + rssid + " ]";
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Collection<Contentstbl> getContentstblCollection() {
        return contentstblCollection;
    }

    public void setContentstblCollection(Collection<Contentstbl> contentstblCollection) {
        this.contentstblCollection = contentstblCollection;
    }

    public Categorytbl getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Categorytbl categoryid) {
        this.categoryid = categoryid;
    }
    
}
