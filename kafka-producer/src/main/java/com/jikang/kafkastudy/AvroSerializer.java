package com.jikang.kafkastudy;

import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.logging.Logger;

public class AvroSerializer <T extends SpecificRecordBase> implements Serializer<T> {

    private final static Logger LOG = Logger.getGlobal();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, T payload) {
        byte[] bytes = null;
        try {
            if(payload != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
                DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(payload.getSchema());

                datumWriter.write(payload, binaryEncoder);
                binaryEncoder.flush();
                byteArrayOutputStream.close();
                bytes = byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            LOG.warning("Unable to serialize" + e);
        }
        return bytes;
    }

    @Override
    public void close(){

    }
}
