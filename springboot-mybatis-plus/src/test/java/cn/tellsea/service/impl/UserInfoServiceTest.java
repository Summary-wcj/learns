package cn.tellsea.service.impl;

import cn.tellsea.SpringbootMybatisPlusApplicationTests;
import cn.tellsea.entity.UserInfo;
import cn.tellsea.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserInfoServiceTest extends SpringbootMybatisPlusApplicationTests {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void save() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("mybatis plus");
        int count = userInfoMapper.insert(userInfo);
        if (count != 1) {
            System.out.println("error");
        } else {
            System.out.println("success");
        }
    }

    @Test
    public void list() {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.getSqlSelect();
        userInfoMapper.selectObjs(queryWrapper);
    }
}
