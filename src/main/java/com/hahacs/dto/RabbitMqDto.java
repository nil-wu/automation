package com.hahacs.dto;

public class RabbitMqDto {

    private Integer payload_bytes;
    private boolean redelivered;
    private String exchange;
    private String routing_key;
    private Integer message_count;

    private RabbitMqProperties properties;

    private String payload;
    private String payload_encoding;

    public Integer getPayload_bytes() {
        return payload_bytes;
    }

    public void setPayload_bytes(Integer payload_bytes) {
        this.payload_bytes = payload_bytes;
    }

    public boolean isRedelivered() {
        return redelivered;
    }

    public void setRedelivered(boolean redelivered) {
        this.redelivered = redelivered;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRouting_key() {
        return routing_key;
    }

    public void setRouting_key(String routing_key) {
        this.routing_key = routing_key;
    }

    public Integer getMessage_count() {
        return message_count;
    }

    public void setMessage_count(Integer message_count) {
        this.message_count = message_count;
    }

    public RabbitMqProperties getProperties() {
        return properties;
    }

    public void setProperties(RabbitMqProperties properties) {
        this.properties = properties;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload_encoding() {
        return payload_encoding;
    }

    public void setPayload_encoding(String payload_encoding) {
        this.payload_encoding = payload_encoding;
    }
}
