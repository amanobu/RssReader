/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ama.rssreader.cdis;

import ama.rssreader.ejbs.RssReaderLogic;
import ama.rssreader.util.RssUtil;
import ama.rssreader.validator.Feed;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

/**
 *
 * @author amanobu
 */
@Named(value = "feedRegistPage")
@RequestScoped
public class FeedRegistPage implements Serializable {
    //Validatorは上から順にではなく全てチェックされるみたいっすね
    //Groupってのを指定すると細かく制御出来る模様
    //つうじょうは暗黙的にjavax.validation.groups.Defaultが設定されているらしい
    @Pattern(regexp = "^http.*", message = "URLの形式がおかしいよ(httpで始まっていない)")
    @Feed(message="おかしくないっすか？")
    private String url;
    @EJB
    RssReaderLogic rsslogic;

    /**
     * Creates a new instance of FeedRegistPage
     */
    public FeedRegistPage() {
    }

    //@Pattern(regexp = "feedreg-success",message="登録失敗！")
    //こうやったら画面遷移せずに入力チェックエラーと同じ感じになると思ったけどならない
    public String registFeed() {
        try {
            String title = RssUtil.getTitle(getUrl());

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext econtext = context.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) econtext.getRequest();
            String userid = request.getRemoteUser();
            Logger.getLogger(FeedListPage.class.getName()).log(Level.SEVERE, "getFeedList called:" + request.getRemoteUser() + "," + title + "," + url, "");
            rsslogic.regFeed(userid, url);
            return "feedreg-success";
        } catch (Exception e) {
            e.printStackTrace();
            return "FeedRegistError";
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
