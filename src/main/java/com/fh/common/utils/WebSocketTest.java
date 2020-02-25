package com.fh.common.utils;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/webSocket/{param}")
public class WebSocketTest {

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount;
    //实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key为用户标识
    private static Map<String, WebSocketTest> connections = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String role;
    private String socketId;

    /**
     * 连接建立成功调用的方法，将用户信息放入connections中
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("param") String param, Session session) {
        this.session = session;
//        String[] arr = param.split(",");
//        this.role = arr[0];             //用户标识
        this.role = param;             //用户标识
//        this.socketId = arr[1];         //会话标识
        connections.put(role, this);     //添加到map中
        addOnlineCount();               // 在线数加
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        connections.remove(role);  // 从map中移除
        subOnlineCount();          // 在线数减
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        /**
         * 解析json数据
         */
        JSONObject obj = JSONObject.parseObject(message);

        String info = null;  //需要发送的信息
        String to = null;      //目标

        if (obj.containsKey("message")) {
            info = (String) obj.get("message");
        }


        if (obj.containsKey("role")) {
            String tos = (String) obj.get("role");
            //如果用户为多个
            if (tos.contains(",")) {
                String[] split = tos.split(",");
                for (String str : split) {
                    send(info, role, str);
                }
            } else {
                to = (String) obj.get("role");
                send(info, role, to);
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    /**
     * 发送信息
     *
     * @param msg      需要发送的信息
     * @param from     谁发送
     * @param to       谁接收
     */
    public static void send(String msg, String from, String to) {
        try {
            //发送给to指定用户
            WebSocketTest con = connections.get(to);
            if (con != null) {
            	con.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }
}
