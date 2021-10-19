package com.jikang.rabbitmq.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class SampleMessage implements Serializable {
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        }
        catch(Exception e){
            System.out.println("err to parsing");
            return "";
        }
    }
}
