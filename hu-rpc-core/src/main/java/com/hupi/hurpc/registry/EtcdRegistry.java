package com.hupi.hurpc.registry;



import cn.hutool.json.JSONUtil;
import com.hupi.hurpc.config.RegistryConfig;
import com.hupi.hurpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author xxx
 * @date 2024/3/8 8:47
 */
public class EtcdRegistry implements Registry {
    private Client client;

    private KV kvClient;


    //根节点
    private static final String ETCD_ROOT_PATH="/rpc/";

    public static void main(String[] args) throws ExecutionException,InterruptedException {
        Client client=Client.builder().endpoints("http://localhost:2379")
                .build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        //put the key value
        kvClient.put(key,value).get();

        //get the CompletableFuture
        CompletableFuture<GetResponse> getFuture=kvClient.get(key);

        //get the value from CompletableFuture
        GetResponse response=getFuture.get();

        //delete the key
        kvClient.delete(key).get();
    }

    @Override
    public void init(RegistryConfig registryConfig) {
        client=Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient=client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建Lease 和 KV客户端
        Lease leaseClient=client.getLeaseClient();

        //创建一个30秒的租约
        long leaseId=leaseClient.grant(30).get().getID();

        //设置要存储的键值对
        String registerKey= ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key,value,putOption).get();

    }

    //服务注销，删除key
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey(),StandardCharsets.UTF_8));

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //前缀搜索，结尾一定要加'/'
        String searchPrefix=ETCD_ROOT_PATH+serviceKey+"/";

        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            //解析服务信息
            return keyValues.stream()
                    .map(keyValue -> {
                        String value=keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
        }catch (Exception e){
             throw new RuntimeException("获取服务列表失败",e);
        }
    }


    //注册中心销毁，用于项目关闭后释放资源
    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        //释放资源
        if (kvClient!=null){
            kvClient.close();
        }
        if (client!=null){
            client.close();
        }

    }
}