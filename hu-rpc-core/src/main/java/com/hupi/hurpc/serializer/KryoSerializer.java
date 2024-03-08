package com.hupi.hurpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author xxx
 * @date 2024/3/7 15:15
 * Kryo序列化器
 */
public class KryoSerializer implements Serializer{
    //kryo线程不安全，使用ThreadLocal保证每个线程只有一个Kryo

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL=ThreadLocal.withInitial(()->{
        Kryo kryo=new Kryo();
        //设置动态序列化和反序列化类，不提前注册所有类(可能有安全问题)
        kryo.setRegistrationRequired(false);
        return kryo;
    });
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        KRYO_THREAD_LOCAL.get().writeObject(output,object);
        output.close();

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> classType) throws IOException {
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        T reslut=KRYO_THREAD_LOCAL.get().readObject(input,classType);
        input.close();
        return reslut;
    }
}
