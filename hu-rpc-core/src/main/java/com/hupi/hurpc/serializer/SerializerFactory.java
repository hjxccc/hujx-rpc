package com.hupi.hurpc.serializer;

import com.hupi.hurpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xxx
 * @date 2024/3/7 15:33
 * 序列化工厂(用于获取序列化器对象)
 */
public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    //序列化映射(用于实现单例)
    private static final Map<String,Serializer> KEY_SERIALIZER_MAP=
            new HashMap<String,Serializer>(){{
       put(SerializerKeys.JDK,new JdkSerializer());
       put(SerializerKeys.JSON,new JsonSerializer());
       put(SerializerKeys.KRYO,new KryoSerializer());
       put(SerializerKeys.HESSIAN,new HessianSerializer());
    }};


    //默认序列化器
//     private static final Serializer DEFAULT_SERIALIZER=KEY_SERIALIZER_MAP.get("jdk");
    private static final Serializer DEFAULT_SERIALIZER=new JdkSerializer();

     //获取实例
//     public static Serializer getInstance(String key){
//        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
//    }
     public static Serializer getInstance(String key){
         return SpiLoader.getInstance(Serializer.class,key);
     }
}
