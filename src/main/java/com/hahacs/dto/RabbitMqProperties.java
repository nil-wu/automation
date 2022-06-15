package com.hahacs.dto;

public class RabbitMqProperties {

    private Integer priority;
    private Integer delivery_mode;

    private RabbitMqHeaders headers;

    private String content_encoding;
    private String content_type;

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getDelivery_mode() {
        return delivery_mode;
    }

    public void setDelivery_mode(Integer delivery_mode) {
        this.delivery_mode = delivery_mode;
    }

    public RabbitMqHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(RabbitMqHeaders headers) {
        this.headers = headers;
    }

    public String getContent_encoding() {
        return content_encoding;
    }

    public void setContent_encoding(String content_encoding) {
        this.content_encoding = content_encoding;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
}
