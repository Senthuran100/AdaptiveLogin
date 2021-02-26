package com.example.login.csvwriter;

import com.example.login.model.UserLoginParam;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<UserLoginParam, UserLoginParam> {
    @Override
    public UserLoginParam process(UserLoginParam user) throws Exception {
        return user;
    }
}
