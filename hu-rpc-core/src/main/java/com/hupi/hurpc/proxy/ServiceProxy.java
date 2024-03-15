package com.hupi.hurpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.hupi.hurpc.RpcApplication;
import com.hupi.hurpc.config.RpcConfig;
import com.hupi.hurpc.constant.RpcConstant;
import com.hupi.hurpc.model.RpcRequest;
import com.hupi.hurpc.model.RpcResponse;
import com.hupi.hurpc.model.ServiceMetaInfo;
import com.hupi.hurpc.registry.Registry;
import com.hupi.hurpc.registry.RegistryFactory;
import com.hupi.hurpc.serializer.Serializer;
import com.hupi.hurpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xxx
 * @date 2024/3/5 18:45
 * 动态代理(JDK动态代理)
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
       final Serializer serializer= SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName=method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig=RpcApplication.getRpcConfig();
            Registry registry= RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo=new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }
            //暂时先取第一个
            ServiceMetaInfo selectedServiceMetaInfo=serviceMetaInfoList.get(0);
            // 发送请求
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}