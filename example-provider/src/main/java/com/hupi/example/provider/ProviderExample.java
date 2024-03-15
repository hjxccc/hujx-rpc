package com.hupi.example.provider;

import com.hupi.example.common.service.UserService;
import com.hupi.hurpc.LocalRegistry.LocalRegistry;
import com.hupi.hurpc.RpcApplication;
import com.hupi.hurpc.config.RegistryConfig;
import com.hupi.hurpc.config.RpcConfig;
import com.hupi.hurpc.model.ServiceMetaInfo;
import com.hupi.hurpc.registry.Registry;
import com.hupi.hurpc.registry.RegistryFactory;
import com.hupi.hurpc.server.VertxHttpServer;
import com.hupi.hurpc.server.Httpserver;

/**
 * @author xxx
 * @date 2024/3/8 15:35
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {
        //RPC 框架初始化
        RpcApplication.init();

        //注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig=RpcApplication.getRpcConfig();
        RegistryConfig registryConfig=rpcConfig.getRegistryConfig();
        Registry registry= RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());

        try {
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException();
        }
        //启动web服务
        Httpserver httpServer=new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}
