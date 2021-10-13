package com.jikang.kafkastudy;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SimpleConsumer {
    private final static Logger LOG = Logger.getGlobal();
    private final static String TOPIC_NAME = "test";
    private final static String GROUP_ID = "testgroup";
    private final static String BOOTSTRAP_SERVERS = "3.35.19.153:9092";
    // 멀티스레드를 사용할 경우
    private final static int CONSUMER_COUNT = 3;
    private final static List<ConsumerWorker> workerThreads = new ArrayList<>();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, )
        // 자동 커밋 옵션 설정, 기본 값은 true
        //configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        // 자동 커밋의 간격 설정
        //configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 60000);

        // 멀티스레드 사용시 하단 내용을 스레드 내에서 선언한다.
        /*KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        while (true){
            // poll() 실행시 자동으로 커밋을 실행한다.
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for(ConsumerRecord<String, String> record : records){
                LOG.info(record.value());
                consumer.commitSync();
                record.offset();
            }
        }*/

        // 스레드 실행
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 0; i < CONSUMER_COUNT; i++){
            ConsumerWorker worker = new ConsumerWorker(configs, TOPIC_NAME, i);
            workerThreads.add(worker);
            executorService.execute(worker);
        }
    }

    static class ShutdownThread extends Thread {
        public void run(){
            workerThreads.forEach(ConsumerWorker::shutdown);
            LOG.info("Bye");
        }
    }
}
