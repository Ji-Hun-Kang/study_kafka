package com.jikang.kafkastudy;

import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AvroDeserializer<T extends GenericContainer> implements Deserializer<T> {

    protected final Class<User> targetType;
    private final static Logger LOGGER = LoggerFactory.getLogger(AvroDeserializer.class);

    public AvroDeserializer() {
        LOGGER.info("AvroDeseriallizer no-argument constructer start");
        this.targetType = User.class;
    }

    public AvroDeserializer(Class<User> targetType){
        LOGGER.info("AvroDeseriallizer constructer start");
        this.targetType = targetType;
    }

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        T returnObject = null;
        try{
            if(data != null){
                DatumReader<GenericRecord> datumReader = new GenericDatumReader(targetType.newInstance().getSchema());
                Decoder decoder = DecoderFactory.get().binaryDecoder(data, null);
                returnObject = (T) datumReader.read(null, decoder);
            }
        } catch (Exception e) {
            LOGGER.debug("Unable to Deserialize"+e);
        }
        return returnObject;
    }

    @Override
    public void close(){

    }
}
