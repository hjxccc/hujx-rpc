package com.hupi.example.common.service;

import com.hupi.example.common.model.User;

/**
 * @author xxx
 * @date 2024/3/5 15:54
 */
public interface UserService {
    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);


    //新方法  -获取数字
    default short getNumber(){
        return 1;
    }
}
