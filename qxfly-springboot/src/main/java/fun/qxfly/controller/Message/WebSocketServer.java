package fun.qxfly.controller.Message;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import fun.qxfly.common.domain.entity.Message;
import fun.qxfly.service.User.MessageService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/message/{uid}")
@Component
@Slf4j
public class WebSocketServer {

    private static final Map<Integer, Session> SESSION_HASH_MAP = new ConcurrentHashMap<>();

    /**
     * 无法使用自动注入，
     * 解决方案：提供一个spring context上下文
     */
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServer.applicationContext = applicationContext;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("uid") Integer uid) {
        log.info("连接建立 uid: {}", uid);
        SESSION_HASH_MAP.put(uid, session);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("uid") Integer uid) throws Exception {
        JSONObject json = JSON.parseObject(message);
        String msg = (String) json.get("message");
        Integer toUid = (Integer) json.get("toUid");
        String msgId = (String) json.get("msgId");
        Message msgObj = new Message();
        msgObj.setMsgId(msgId);
        msgObj.setFromUid(uid);
        msgObj.setToUid(toUid);
        msgObj.setContent(msg);
        MessageService messageService = applicationContext.getBean(MessageService.class);
        String s = messageService.sendMessage(msgObj);
        if (s == null) {
            msgObj.setSendSuccess(false);
            msgObj.setToUid(uid);
            msgObj.setContent("发送失败");
            session.getBasicRemote().sendText(JSONObject.toJSONString(msgObj));
        } else {
            msgObj.setSendSuccess(true);
            Session toUidSession = SESSION_HASH_MAP.get(toUid);
            if (toUidSession != null) {
                toUidSession.getBasicRemote().sendText(JSONObject.toJSONString(msgObj));
            }
            msgObj.setToUid(uid);
            msgObj.setContent("发送成功");
            session.getBasicRemote().sendText(JSONObject.toJSONString(msgObj));
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("uid") Integer uid) {
        log.error("用户下线 uid: {}", uid);
        SESSION_HASH_MAP.remove(uid);
    }


}
