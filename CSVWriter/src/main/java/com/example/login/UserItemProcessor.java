package com.example.login;

import com.example.login.model.UserLoginParam;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<UserLoginParam,UserLoginParam>{
    @Override
    public UserLoginParam process(UserLoginParam userLoginParam) throws Exception {
        return userLoginParam;
    }
}
