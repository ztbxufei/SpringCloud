package com.zhatianbang.config.lua;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

/**
 * @Author: jingfei
 * @Date: 2019/4/15 15:35
 * @Version 1.0
 */
@Configuration
public class LuaConfig {
    @Bean
    public DefaultRedisScript<List> redisScript(){
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/redis.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }
}
