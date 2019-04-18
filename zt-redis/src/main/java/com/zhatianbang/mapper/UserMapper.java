package com.zhatianbang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by lenovo on 2019/4/15.
 */
@Mapper
@Component
public interface UserMapper {

    Map<String,Object> getUserByUsername(String username);

    Map<String,Object> getApplyInfo(String apply_no);
}
