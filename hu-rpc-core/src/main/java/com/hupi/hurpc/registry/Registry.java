package com.hupi.hurpc.registry;

import com.hupi.hurpc.config.RegistryConfig;
import com.hupi.hurpc.model.ServiceMetaInfo;
import com.hupi.hurpc.serializer.SerializerFactory;
import io.etcd.jetcd.common.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xxx
 * @date 2024/3/8 9:55
 * 注册中心
 */
public interface Registry {
    //初始化
    void init(RegistryConfig registryConfig);

    //注册服务(客户端)
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    //注销服务(服务端)
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    //服务发现(获取某服务的所有节点，消费端)
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    //服务销毁
    void destroy();


    //心跳检测（服务端）
    void heartBeat();

    //监听（消费端）
    void watch(String serviceNodeKey);





}
