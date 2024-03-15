package com.hupi.example.consumer;

import com.hupi.example.common.model.User;
import com.hupi.example.common.service.UserService;
import com.hupi.hurpc.proxy.ServiceProxyFactory;

/**
 * @author xxx
 * @date 2024/3/5 23:11
 */
public class ConsumerExample {
    public static void main(String[] args) {
        //动态代理
        UserService userService= ServiceProxyFactory.getProxy(UserService.class);

        //需要获取Userservice的实现类对象
        User user=new User();
        user.setName("hupi");
        //调用
        User newUser=userService.getUser(user);
        if(newUser!=null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user==null");
        }
        long number=userService.getNumber();
        System.out.println(number);
    }
}
