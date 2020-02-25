package com.fh.project.service;

import org.springframework.stereotype.Component;

public interface ChatService {
    void addChat(String from,String to, String content);
}
