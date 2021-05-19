package com.springbootfundamentals.springbootrestservice.controller;

import org.springframework.stereotype.Component;

@Component
public class Greetings {
    private long id;
    private String context;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
