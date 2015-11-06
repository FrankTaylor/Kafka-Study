package example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;

/**
 * Kafka Producer 的测试。 
 * 
 * @author FrankTaylor
 * @since 2015/11/3
 *
 */
public class MyProducer {
	
	/** producer.properties 文件的路径。*/
	private final String PROPERTIES_FILEPATH;
	/** 执行 Producer 发送消息的线程数。*/
	private final int THREAD_NUMS;
	
	/** Kafka Topic 名称集合。*/
	private final List<String> TOPIC_LIST;
	
	public MyProducer() {
		PROPERTIES_FILEPATH = "config/kafka/producer.properties";
		THREAD_NUMS = 1;
		
		TOPIC_LIST = new ArrayList<String>();
		TOPIC_LIST.add("shushang");
//		TOPIC_LIST.add("dianshang");
	}
	
	public static void main(String[] args) {
		MyProducer myProducer = new MyProducer();
		myProducer.testSendMessage();
	}
	
	private void testSendMessage() {
		ExecutorService exec = Executors.newFixedThreadPool(THREAD_NUMS);
		
		for (int i = 0; i < 1; i++) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					try {
						KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(getProperties());
						List<PartitionInfo> partitionList = kafkaProducer.partitionsFor("abc");
						
						System.out.println(partitionList);
						System.out.println(partitionList.size());
						
//						ProducerRecord<String, String> record = new ProducerRecord<String, String>("abc", "aaaaaaaaaa", "bbbbbbbbbbbbbb");
//						
//						Future<RecordMetadata> f = kafkaProducer.send(record);
//						
//						RecordMetadata metadata = f.get();
//						System.out.println("已发送元数据 ：topic = " + metadata.topic() + ", partition = " + metadata.partition() + ", offset = " + metadata.offset());
						
//						for (int i = 0; i < 100000000; i++) {
//							for (String topic : TOPIC_LIST) {
//								ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, topic + "_key_" + i, topic + "_value_" + i);
//								
//								Future<RecordMetadata> f = kafkaProducer.send(record);
//								
//								RecordMetadata metadata = f.get();
//								System.out.println("已发送元数据 ：topic = " + metadata.topic() + ", partition = " + metadata.partition() + ", offset = " + metadata.offset());
//							}
//						}
						
						kafkaProducer.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		exec.shutdown();
	}
	
	private Properties getProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILEPATH));
		return properties;
	}
}