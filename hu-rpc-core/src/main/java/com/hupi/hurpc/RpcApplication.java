package com.hupi.hurpc;

import com.hupi.hurpc.config.RegistryConfig;
import com.hupi.hurpc.config.RpcConfig;
import com.hupi.hurpc.constant.RpcConstant;
import com.hupi.hurpc.registry.Registry;
import com.hupi.hurpc.registry.RegistryFactory;
import com.hupi.hurpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xxx
 * @date 2024/3/5 22:25
 * RPC 框架应用
 * 相当于holder  存放了项目全局用到的变量，双检索单例模式实现
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;


    //获取配置
    public static RpcConfig getRpcConfig(){
        if(rpcConfig==null){
            synchronized (RpcApplication.class){
                if (rpcConfig==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }

    //框架初始化，支持传入自定义配置

    public static void init(RpcConfig newRpcConfig){
        rpcConfig=newRpcConfig;
        log.info("rpc init,config={}",newRpcConfig.toString());
        //注册中心初始化
        RegistryConfig registryConfig=rpcConfig.getRegistryConfig();
        Registry registry= RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init,config={}",registryConfig);
    }

    //初始化
    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig= ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            //配置加载失败，加载默认值
            newRpcConfig=new RpcConfig();
        }
        init(newRpcConfig);
    }


}
