/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ama.rssreader.entities;

import ama.rssreader.util.LogUtil;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "CONTENTSTBL")
@NamedQueries({
    @NamedQuery(name = "Contentstbl.findAll", query = "SELECT c FROM Contentstbl c"),
    @NamedQuery(name = "Contentstbl.findByContentsid", query = "SELECT c FROM Contentstbl c WHERE c.contentsid = :contentsid"),
    @NamedQuery(name = "Contentstbl.findByReadflg", query = "SELECT c FROM Contentstbl c WHERE c.readflg = :readflg"),
    @NamedQuery(name = "Contentstbl.findByTitle", query = "SELECT c FROM Contentstbl c WHERE c.title = :title"),
    @NamedQuery(name = "Contentstbl.findByUrl", query = "SELECT c FROM Contentstbl c WHERE c.url = :url"),
    @NamedQuery(name = "Contentstbl.findByContents", query = "SELECT c FROM Contentstbl c WHERE c.contents = :contents"),
    @NamedQuery(name = "Contentstbl.findByPublishdate", query = "SELECT c FROM Contentstbl c WHERE c.publishdate = :publishdate"),
    @NamedQuery(name = "Contentstbl.findByRegdate", query = "SELECT c FROM Contentstbl c WHERE c.regdate = :regdate"),
    @NamedQuery(name = "Contentstbl.findByUpddate", query = "SELECT c FROM Contentstbl c WHERE c.upddate = :upddate")})
public class Contentstbl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONTENTSID")
    private Integer contentsid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "READFLG")
    private Boolean readflg;
    @Size(max = 1024)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 1024)
    @Column(name = "URL")
    private String url;
    @Size(max = 32672)
    @Column(name = "CONTENTS")
    private String contents;
    @Column(name = "PUBLISHDATE")
    @Temporal(TemporalType.DATE)
    private Date publishdate;
    @Column(name = "REGDATE")
    @Temporal(TemporalType.DATE)
    private Date regdate;
    @Column(name = "UPDDATE")
    @Temporal(TemporalType.DATE)
    private Date upddate;
    @JoinColumn(name = "RSSID", referencedColumnName = "RSSID")
    @ManyToOne(optional = false)
    private Feedtbl rssid;

    public Contentstbl() {
    }

    public Contentstbl(Integer contentsid) {
        this.contentsid = contentsid;
    }

    public Contentstbl(Integer contentsid, Boolean readflg) {
        this.contentsid = contentsid;
        this.readflg = readflg;
    }

    public Integer getContentsid() {
        return contentsid;
    }

    public void setContentsid(Integer contentsid) {
        this.contentsid = contentsid;
    }

    public Boolean getReadflg() {
        return readflg;
    }

    public void setReadflg(Boolean readflg) {
        this.readflg = readflg;
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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
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

    public Feedtbl getRssid() {
        return rssid;
    }

    public void setRssid(Feedtbl rssid) {
        this.rssid = rssid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contentsid != null ? contentsid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contentstbl)) {
            return false;
        }
        Contentstbl other = (Contentstbl) object;
        if ((this.contentsid == null && other.contentsid != null) || (this.contentsid != null && !this.contentsid.equals(other.contentsid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return LogUtil.stringBuilderJoin(LogUtil.obj2StringArray("ama.rssreader.cdis.Contentstbl[---","contentsid:",contentsid,"rssid:",rssid,"reedflg:",readflg,"title:",title,"url:",url,"contents:",contents,"publishdate:",publishdate,"regdate:",regdate,"upddate:",upddate,"---]"));
    }
    
}
