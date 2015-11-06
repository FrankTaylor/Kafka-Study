package example;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * Kafka Consumer 的测试。 
 * 
 * @author FrankTaylor
 * @since 2015/11/3
 *
 */
public class MyConsumer {

	public static void main(String[] args) {
		
		try {
			new Thread(new ConsumerTask("config/kafka/consumer1.properties")).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class ConsumerTask implements Runnable {
		
		/** consumer.properties 文件的路径。*/
		private final String PROPERTIES_FILEPATH;
		
		public ConsumerTask(String filepath) {
			PROPERTIES_FILEPATH = filepath;
		}
		
		@Override
		public void run() {
			try {
				
				Properties properties = new Properties();
				properties.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILEPATH));
				
				ConsumerConnector cc = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
				
				// Kafka Topic 名称和分片的集合。
				Map<String, Integer> topicMap = new HashMap<String, Integer>();
				topicMap.put("shushang", 1);
				
		        Map<String, List<KafkaStream<byte[], byte[]>>> streams = cc.createMessageStreams(topicMap);
		        
		        
		        List<KafkaStream<byte[], byte[]>> partitions = streams.get("shushang");
		        ConsumerIterator<byte[], byte[]> it = partitions.get(0).iterator();
		        
                while (it.hasNext()) {
                    MessageAndMetadata<byte[],byte[]> item = it.next();
                    
                    System.out.println("partiton = " + item.partition());
                    System.out.println("offset = " + item.offset());
                    System.out.println("message = " + new String(item.message()));
                    
                    try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) { e.printStackTrace(); }
                }
		        
		        
//		        for (Map.Entry<String, Integer> entry : topicMap.entrySet()) {
//		        	
//		        	String topicName = entry.getKey();
//		        	
//		        	List<KafkaStream<byte[], byte[]>> partitions = streams.get(topicName);
//		        	
//		        	ExecutorService exec = Executors.newSingleThreadExecutor();
//		        	
//		        	for(final KafkaStream<byte[], byte[]> partition : partitions){
//		            	exec.execute(new Runnable() {
//		            		
//		            		@Override
//		            		public void run() {
//		            			ConsumerIterator<byte[], byte[]> it = partition.iterator();
//		                        while(it.hasNext()){
//		                            MessageAndMetadata<byte[],byte[]> item = it.next();
//		                            
//		                            System.out.println("partiton = " + item.partition());
//		                            System.out.println("offset = " + item.offset());
//		                            System.out.println("message = " + new String(item.message()));
//		                            
//		                            try {
//										TimeUnit.SECONDS.sleep(1);
//									} catch (InterruptedException e) { e.printStackTrace(); }
//		                        }
//		            		}
//		            	});
//		            }
//		        	
//		        	exec.shutdown();
//		        	
//		        }
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
}