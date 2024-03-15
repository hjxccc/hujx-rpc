package com.hupi.example.provider;

import com.hupi.example.common.service.UserService;
import com.hupi.hurpc.LocalRegistry.LocalRegistry;
import com.hupi.hurpc.RpcApplication;
import com.hupi.hurpc.server.Httpserver;
import com.hupi.hurpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 *
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        //存入class对象可以调用。
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        //启动web服务
        Httpserver httpserver=new VertxHttpServer();
        httpserver.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}