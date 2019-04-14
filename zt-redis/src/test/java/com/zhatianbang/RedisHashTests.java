package com.zhatianbang;

import com.zhatianbang.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisHashTests {

	@Autowired
	RedisUtil redisUtil;

	/**
	 * 测试 将map值存入hash
	 */
	@Test
	public void hMSetTest() {
		Map<String,Object> Rmap = new HashMap<>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("carIdCodeType","车辆唯一标示类型");
		map.put("wheName","仓库名称");
		map.put("carSeries","车系");
		map.put("vehicleModel","车型");
		map.put("carBrank","品牌");
		map.put("tsnDsc","排量");
		map.put("porType","变速箱描述");
		map.put("carMktTme","年份款");
		map.put("carJitVte","国产合资进口");
		map.put("carClr","颜色");
		map.put("carFaeId","BBB");
		Rmap.put("AAA",map);
		redisUtil.hMSet("新车_k5_华东库",Rmap);
	}

	/**
	 * 测试 根据key取出hash值
	 */
	@Test
	public void hMGetTest(){
		Map<Object, Object> objectObjectMap = redisUtil.hMGet("新车:k5:华东库");
		System.out.println(objectObjectMap);
	}

	/**
	 * 测试 根据key删除hash value值
	 */
	@Test
	public void hDelTest(){
		redisUtil.hDel("新车:k5:华东库","carJitVte","carClr");
	}

	/**
	 * 测试 根据key删除hash value值
	 */
	@Test
	public void delTest(){
		redisUtil.del("新车:k5:华东库");
	}

	/**
	 * 测试 向一张hash表中放入数据,如果不存在将创建
	 */
	@Test
	public void hSetTest(){
		redisUtil.hSet("新车:k5:华东库","carJitVte","国产合资进口");
		redisUtil.hSet("新车:k5:华东库","carClr","黑色");
	}

	/**
	 * 根据传入的str 模糊查询所有的keys
	 */
	@Test
	public void keysTest(){
		System.out.println(redisUtil.keys("新车_k5"));
	}

}
