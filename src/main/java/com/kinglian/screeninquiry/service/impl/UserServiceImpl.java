/**
 * Copyright (C), 2018-2019,Kinglian
 * FileName: UserServiceImpl
 * Author:   weiyz
 * Date:     2019/1/16 19:48
 * Description: User实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.kinglian.screeninquiry.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kinglian.screeninquiry.dao.UserMapper;
import com.kinglian.screeninquiry.model.entity.User;
import com.kinglian.screeninquiry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private UserMapper userMapper;

    /**
     * 判断该账号密码是否存在
     *
     * @param user
     * @param password
     * @return
     */
    @Override
    public boolean login(String user, String password) {
        Integer result = userMapper.selectCount(new EntityWrapper<User>().eq("user_account", user).eq("password", password));
        return result > 0;
    }
}
