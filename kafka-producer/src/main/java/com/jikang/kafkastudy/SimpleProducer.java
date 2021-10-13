package com.jikang.kafkastudy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

public class SimpleProducer {
    private final static Logger LOG = Logger.getGlobal();
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "3.35.19.153:9092";
    // 파티션 번호를 지정할 수도 하지 않을 수도 있습니다.
    private final static int PARTITION_NUMBER = 1;

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, File> producer = new KafkaProducer<>(configs);

        //for(int index = 0; index < 10; index++){
            //String data = "This is record " + index;
            //ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, data);
            File file = new File("users.avro");
            ProducerRecord<String, File> record = new ProducerRecord<>(TOPIC_NAME, file);
            //ProducerRecord<> 생성자의 매개변수가 3개일 경우 두 번째 인자는 키값에 해당합니다.
            //ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, Integer.toString(index), data);
            //ProducerRecord<> 생성자의 매개변수가 4개일 경우 두 번째 인자는 파티션 번호에 해당합니다.
            //ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, PARTITION_NUMBER, Integer.toString(index), data);

            try{
                producer.send(record);
                LOG.info("Send to " + TOPIC_NAME + " | data : " + file);
                Thread.sleep(1000);
            } catch (Exception e) {
                LOG.warning(e.getMessage());
            }
        //}
    }
}
