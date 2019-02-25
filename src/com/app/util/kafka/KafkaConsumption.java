package com.app.util.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import com.app.conf.AppConfig;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

/**
 * 消息队列消费工具类
 * **/
public class KafkaConsumption 
{

	private final ConsumerConnector consumer;
	
	/**
	 * 
	 * **/
	public KafkaConsumption(String host,String groupId)
	{
        Properties props = new Properties();
	               props.put("zookeeper.connect", host);//zookeeper 配置
	               props.put("group.id", groupId);//group 代表一个消费组
	               props.put("zookeeper.session.timeout.ms", "4000"); // 连接zk的session超时时间
	               props.put("zookeeper.sync.time.ms", "200");//zk follower落后于zk leader的最长时间        props.put("auto.commit.interval.ms", "1000");//往zookeeper上写offset的频率        props.put("auto.offset.reset", "smallest");//如果offset出了返回，则 smallest: 自动设置reset到最小的offset. largest : 自动设置offset到最大的offset. 其它值不允许，会抛出异常        
	               props.put("serializer.class", "kafka.serializer.StringEncoder");//序列化类 
	       
	    ConsumerConfig config = new ConsumerConfig(props);
	    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
	}
	
	/**
	 * 监听topics 队列
	 * @param kafkaParam kafak参数
	 * **/
	public void consume(KafkaParam... kafkaParam)
	{
    	//接收主题名称
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        int threadNum                      = 0; //消费者迭代器数量
        for(KafkaParam topic : kafkaParam)
        {
        	topicCountMap.put(topic.getName(), topic.getNum()); //主题名称  线程数
        	threadNum += topic.getNum();
        }
        
        ExecutorService executorService = AppConfig.getAppConfig().getFixedThreadPool(threadNum);//用于异步执行消费者的线程池
        StringDecoder keyDecoder        = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder      = new StringDecoder(new VerifiableProperties());
 
        Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
        
        for(KafkaParam topic : kafkaParam)
        {
        	for(int i=0; i<consumerMap.get(topic.getName()).size(); i++)
        	{
        		executorService.execute(new KafkaConsumWork(consumerMap.get(topic.getName()).get(i).iterator(),topic));
        	}
        }
                            
	}
	
    public void close()
    {
    	consumer.shutdown();
    }
}