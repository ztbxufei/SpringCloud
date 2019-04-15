--
-- Created by IntelliJ IDEA.
-- User: jingfei
-- Date: 2019/4/15
-- Time: 15:36
-- To change this template use File | Settings | File Templates.
--
local tables = {};
-- 获取匹配成功的所有key
local keys = redis.call("keys", KEYS[1]);
for i,v in ipairs(keys) do
    local map = {};
    -- 获取key下面所有的字段
    local hKeys = redis.call("hkeys", v);
    -- 获取一条数据所有的字段，得到一条数据的完整值
    for j, k in ipairs(hKeys) do
        -- 获取字段属性
        map[k] = redis.call("hmget", v, k)[1];
    end
    tables[i] = cjson.encode(map);
end
return tables;

