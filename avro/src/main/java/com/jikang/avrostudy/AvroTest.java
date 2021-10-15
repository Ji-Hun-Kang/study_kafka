package com.jikang.avrostudy;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class AvroTest {
    //private final static Logger LOG = Logger.getGlobal();
    public static void main(String[] args) {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        // Serialize user1, user2, and user3 to disk
        try {
            DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
            DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
            dataFileWriter.create(user1.getSchema(), new File("users.avro"));
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
            dataFileWriter.append(user3);
            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize Users from disk
        try {
            File file = new File("users.avro");
            DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
            DataFileReader<User> dataFileReader = new DataFileReader<User>(file, userDatumReader);
            User userReaded = null;
            while(dataFileReader.hasNext()){
                // Reuse userReaded object by passing it to next(). This saves us from
                // allocating and garbage collecting many object for files with
                // many items.
                userReaded = dataFileReader.next(userReaded);
                System.out.println(userReaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
