package com.hupi.hurpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xxx
 * @date 2024/3/5 17:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    //响应数据
    private Object data;

    //响应数据类型(预留)
    private Class<?> dataType;

    //响应信息
    private String message;

    //异常信息
    private Exception exception;


}
