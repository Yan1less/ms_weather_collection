package com.microweather.mvn_collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisCluster;

import java.util.concurrent.TimeUnit;

/**
 * Weather Data Collection Service.
 * 
 * @since 1.0.0 2017年11月26日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Service
public class WeatherDataCollectionServiceImpl implements WeatherDataCollectionService {
	private static final String WEATHER_URI = "http://wthrcdn.etouch.cn/weather_mini?";

	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JedisCluster cluster;
	
	@Override
	public void syncDateByCityId(String cityId) {
		String uri = WEATHER_URI + "citykey=" + cityId;
		this.saveWeatherData(uri);
	}
	
	/**
	 * 把天气数据放在缓存
	 * @param uri
	 */
	private void saveWeatherData(String uri) {
		String key = uri;
		String strBody = null;
		// 调用服务接口来获取
 		ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);

 		if (respString.getStatusCodeValue() == 200) {
			strBody = respString.getBody();
		}
		
		// 数据写入缓存
		cluster.append(key,strBody);


	}
}
