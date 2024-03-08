package com.hupi.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.hupi.example.common.model.User;
import com.hupi.example.common.service.UserService;
import com.hupi.hurpc.serializer.JdkSerializer;
import com.hupi.hurpc.serializer.Serializer;
import com.hupi.hurpc.model.RpcRequest;
import com.hupi.hurpc.model.RpcResponse;

import java.io.IOException;

/**
 * @author xxx
 * @date 2024/3/5 18:33
 * 静态代理
 */
public class UserServiceProxy implements UserService {
    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    @Override
    public User getUser(User user) {
        //指定序列化器
        Serializer serializer=new JdkSerializer();

        //发请求
        RpcRequest rpcRequest=RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse httpResponse= HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute())
                {
                    result=httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse=serializer.deserialize(result, RpcResponse.class);
            return (User)rpcResponse.getData();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
