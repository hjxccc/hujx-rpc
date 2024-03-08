package com.hupi.hurpc.serializer;

import java.io.IOException;

/**
 * @author xxx
 * @date 2024/3/5 17:05
 * 序列化器接口
 */
public interface Serializer {
    //序列化
    <T> byte[] serialize(T object) throws IOException;

    //反序列化
    <T> T deserialize(byte[] bytes,Class<T> type) throws  IOException;
}
