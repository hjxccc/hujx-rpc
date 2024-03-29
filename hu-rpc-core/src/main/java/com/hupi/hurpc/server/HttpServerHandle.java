package com.hupi.hurpc.server;

import com.hupi.hurpc.LocalRegistry.LocalRegistry;
import com.hupi.hurpc.RpcApplication;
import com.hupi.hurpc.model.RpcRequest;
import com.hupi.hurpc.model.RpcResponse;
import com.hupi.hurpc.serializer.Serializer;
import com.hupi.hurpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.lang.reflect.Method;

/**
 * @author xxx
 * @date 2024/3/5 17:16
 */

//HTTP 请求处理
public class HttpServerHandle implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer= SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //处理日志
        System.out.println("Received request:"+request.method()+" "+request.uri());

        //异步处理HTTP请求

        request.bodyHandler(body ->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest=null;
            try {
                //序列化为 RpcRequest.class
                rpcRequest=serializer.deserialize(bytes, RpcRequest.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            //构造响应结果对象
            RpcResponse rpcResponse=new RpcResponse();
            //如果请求为null,直接返回
            if(rpcRequest==null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                //获取要调用的服务实现类，通过反射调用

                Class<?> implClass= LocalRegistry.get(rpcRequest.getServiceName());
                //类名和参数决定一个实现类
                Method method=implClass.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
                Object result=method.invoke(implClass.newInstance(),rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            }catch (Exception e){
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request,rpcResponse,serializer);
        });
    }

    //响应
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse=request.response()
                .putHeader("content-type","application/json");

        try {
            //序列化为rpcResponse对象
            byte[] serialized=serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        }catch (Exception e){
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }

    }
}
