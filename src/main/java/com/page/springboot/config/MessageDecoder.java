package com.page.springboot.config;

import com.alibaba.fastjson.JSONObject;
import com.page.springboot.req.RepayReq;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

@Slf4j
public class MessageDecoder implements Decoder.Text<RepayReq> {
 
    @Override
    public RepayReq decode(String s) {
        log.info("primal string" + s);
        RepayReq repayReq = null;
        try {
            repayReq = JSONObject.parseObject(s, RepayReq.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return repayReq;
    }
 
    @Override
    public boolean willDecode(String s) {
          
        return (s != null);
    }
 
    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }
 
    @Override
    public void destroy() {
        // do nothing.
    }
}