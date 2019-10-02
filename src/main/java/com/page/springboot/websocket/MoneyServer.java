package com.page.springboot.websocket;

import com.page.springboot.config.MessageDecoder;
import com.page.springboot.config.MessageEncoder;
import com.page.springboot.req.RepayReq;
import com.page.springboot.res.RepayResultRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Page
 * @date 2019-07-02 10:00
 * @description websocket 服务
 */

@Component
@Slf4j
@ServerEndpoint(value = "/money/{userId}", decoders = {
        MessageDecoder.class,}, encoders = {MessageEncoder.class,})
public class MoneyServer {
    private static final Map<String, Session> SESSION_MAP = new HashMap<>();

    @OnOpen
    public void connect(Session session, @PathParam("userId") String userId) {
        // 将session按照房间名来存储，将各个房间的用户隔离
        SESSION_MAP.put(userId, session);
        log.info("websocket成功连接!");
    }

    @OnMessage
    public void repay(RepayReq req, Session session) {
        SESSION_MAP.put(req.getUserId(), session);
        log.info("{}正在还钱:{} 元", req.getName(), req.getMoneyNum());
    }

    public void send(RepayResultRes res) throws IOException, EncodeException {
        if (SESSION_MAP.get(res.getUserId()) == null) {
            log.info("没有找到连接，消息无法推送");
            return;
        }
        SESSION_MAP.get(res.getUserId()).getBasicRemote().sendObject(res);
        log.info("{}成功还钱:{} 元，userId:{},还钱结果:{}", res.getName(),
                res.getMoneyNum(), res.getUserId(), res.isRepayResult()?"还成功" :"失败了");
    }
}
