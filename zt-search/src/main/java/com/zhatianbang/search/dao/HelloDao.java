package com.zhatianbang.search.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: jingfei
 * @Date: 2019/3/6 17:27
 * @Version 1.0
 */
@Mapper
public interface HelloDao {
    int getUserCount();
}
