package com.hupi.hurpc.config;

import com.hupi.hurpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author xxx
 * @date 2024/3/5 22:14
 * RPC框架配置
 */
@Data
public class RpcConfig {
    //名称
    private String name ="hu-rpc";

    //版本号
    private String version="1.0";

    //服务器主机名
    private String serverHost="localhost";

    //服务器端口号
    private Integer serverPort=8082;

    private boolean mock=false;

    private String serializer= SerializerKeys.JDK;

    private RegistryConfig registryConfig=new RegistryConfig();

}
