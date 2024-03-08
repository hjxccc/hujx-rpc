package com.hupi.hurpc.registry;

import com.hupi.hurpc.spi.SpiLoader;

/**
 * @author xxx
 * @date 2024/3/8 10:27
 * 注册中心工厂(用于获取注册中心对象)
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    //默认注册中心
    private static final Registry DEFAULT_REGISTRY=new EtcdRegistry();


    //获取实例
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }

}
