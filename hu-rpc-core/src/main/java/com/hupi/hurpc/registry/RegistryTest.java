package com.hupi.hurpc.registry;


import com.hupi.hurpc.config.RegistryConfig;
import com.hupi.hurpc.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author xxx
 * @date 2024/3/8 14:34
 * 注册中心测试
 */
public class RegistryTest {
    final Registry registry=new EtcdRegistry();




    @Before
    public void init(){
        RegistryConfig registryConfig=new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }
    @Test
    public void heartBeat() throws Exception{
        //init中已经执行心跳检测了
        register();
        //阻塞一分钟
        Thread.sleep(60*1000L);
    }



    @Test
    public void  register() throws Exception{

        ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        serviceMetaInfo.setServiceAddress("localhost:1234");
        registry.register(serviceMetaInfo);


        serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1235);
        serviceMetaInfo.setServiceAddress("localhost:1235");
        registry.register(serviceMetaInfo);


        serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("2.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServiceAddress("localhost:1234");
        serviceMetaInfo.setServicePort(1234);
        registry.register(serviceMetaInfo);

    }

    @Test
    public void unRegister(){
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.unRegister(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery(){
        ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
        Assert.assertNotNull(serviceMetaInfoList);
    }


}
