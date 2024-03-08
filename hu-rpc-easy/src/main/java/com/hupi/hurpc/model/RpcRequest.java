package com.hupi.hurpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xxx
 * @date 2024/3/5 17:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    //服务名称
    private String serviceName;

    //方法名称
    private String methodName;

     //参数类型列表
    private Class<?>[] parameterTypes;

    //参数列表
    private Object[] args;

 }
