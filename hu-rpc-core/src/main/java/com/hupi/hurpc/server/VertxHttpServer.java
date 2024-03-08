package com.hupi.hurpc.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

/**
 * @author xxx
 * @date 2024/3/5 16:15
 */
public class VertxHttpServer implements Httpserver{
    //启动服务器
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建HTTP服务器
        HttpServer server= vertx.createHttpServer();

        //监听端口并处理请求
        server.requestHandler(new HttpServerHandle());

        //启动HTTP服务器并监听指定端口
        server.listen(port,result->{
            if(result.succeeded()){
                System.out.println("Server is now listening on port"+port);
            }else {
                System.out.println("Failed to start server: "+result.cause());
            }
        });


    }
}
