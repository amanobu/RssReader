package ama.rssreader.wr;

import ama.rssreader.util.LogUtil;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;

/**
 * 登録されているFeed全件新着確認をするバッチ処理の起動をするエンドポイント
 *
 * @author amanobu
 */
@Path("updataAll")
@RequestScoped
public class GenericResource {

    //こいつはResource扱いらしいッスね
    //確かにGlassFISHの管理ツールにConcurrent Resourcesがある
    //多分concurrent/__defaultManagedExecutorServiceがInjectされるのだろう
    @Resource
    ManagedExecutorService executeService;

    @Inject
    AllUpdateTask task;

    public GenericResource() {
    }

    @GET
    @Produces("text/html")
    public String exec() {
        LogUtil.log(GenericResource.class.getName(), Level.INFO, "GenericResource.exec called");
        executeService.submit(task);
        //return Response.ok("update ").build();
        return "!!";
    }

}
