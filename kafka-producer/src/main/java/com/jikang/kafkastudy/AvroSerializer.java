package com.jikang.kafkastudy;

import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Map;


//public class AvroSerializer <T extends SpecificRecordBase> implements Serializer<T> {
public class AvroSerializer <T extends GenericContainer> implements Serializer<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AvroSerializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, T payload) {
//    public byte[] serialize(String topic, T data) {
        byte[] bytes = null;
        try {
            if(payload != null) {
//            if(data != null) {
                LOGGER.debug("payload '{}'", payload);
//                LOGGER.debug("data '{}'", data);
//                SerializeUtil serializeUtil = SerializeUtil.getInstance();
//                Schema schema = serializeUtil.getPrimitiveSchema();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                GenericData.Record record = serializeUtil.objectToRecord(data);

                DatumWriter<T> datumWriter = new GenericDatumWriter<>(payload.getSchema());
//                DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
                BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
                datumWriter.write(payload, binaryEncoder);
                binaryEncoder.flush();
                byteArrayOutputStream.close();
                bytes = byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e){
            LOGGER.debug("Unable to serialize" + e);
        }
        return bytes;
    }

    @Override
    public void close(){

    }
}
