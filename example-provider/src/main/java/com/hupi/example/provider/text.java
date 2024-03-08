package com.hupi.example.provider;

import com.hupi.example.common.model.User;
import com.hupi.example.common.service.UserService;

/**
 * @author xxx
 * @date 2024/3/6 10:17
 */
public class text {
    public static void main(String[] args) {
        UserService userService=null;
        User user=new User();
        user.setName("yupi");
        User newUser=userService.getUser(user);
        if(newUser!=null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user==null");
        }
    }
}
