package com.fh.project.service;

import com.fh.project.dao.LoginAndRegisterDao;
import com.fh.project.vo.ChatVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class ChatServiceImpl implements ChatService {
    @Autowired
    private LoginAndRegisterDao dao;
    @Override
    public void addChat(String from, String to, String content) {

        String[] ids = new String[]{from,to};
        Arrays.sort(ids);
        ChatVo chatVo = new ChatVo();
        chatVo.setId(ids[0]+"_"+ids[1]);
        chatVo.setFrom(from);
        chatVo.setTo(to);
        chatVo.setContent(content);
        chatVo.setStatus("0");
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String time = simpleFormatter.format(new Date());
        chatVo.setTime(time);
        dao.addChat(chatVo);
    }
}
