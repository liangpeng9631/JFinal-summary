package com.app.util.kafka;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.app.conf.AppConfig;

/**
 * 队列消息生产工具类
 * **/
public class KafkaProducter 
{
	private final Producer<String, String> producer;
	
	/**
	 * @param host 服务器地址
	 * **/
    public KafkaProducter(String host)
    {
        Properties props = new Properties();
      
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,host);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 0);//批量发送 0禁用此功能
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);//无延迟发送消息
        props.put(ProducerConfig.ACKS_CONFIG, "1");//对应partition的leader写到本地后即返回成功。极端情况下，可能导致失败
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new org.apache.kafka.clients.producer.KafkaProducer<String,String>(props);

    }
    
    /**
     * 发送消息_默认一个分区
     * @param topic    主题
     * @param message  消息
     * **/
    public void send(String topic,String message) throws Exception
    {
    	AppConfig.getAppConfig().getLogger().info("======插入卡夫卡队列send方法====="+topic+" "+message);
        Future<RecordMetadata> fu = producer.send(new ProducerRecord<String,String>(topic, message));
        fu.get().topic();
    }
    
    public void close()
    {
    	producer.close();
    }
}
