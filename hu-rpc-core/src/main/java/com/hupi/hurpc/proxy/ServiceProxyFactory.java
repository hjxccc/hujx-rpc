package com.hupi.hurpc.proxy;



import com.hupi.hurpc.RpcApplication;
import java.lang.reflect.Proxy;

/**
 * @author xxx
 * @date 2024/3/5 18:53
 * 服务代理工厂 (用于创建代理对象)
 */
public class ServiceProxyFactory  {
    //根据服务类获取代理对象
    public static  <T> T getProxy(Class<T> serviceClass) {
        if(RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    //根据服务类获取Mock代理对象
    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());

    }


}
