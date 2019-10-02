package com.page.springboot.config;

import com.alibaba.fastjson.JSONObject;
import com.page.springboot.res.RepayResultRes;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

@Slf4j
public class MessageEncoder implements Encoder.Text<RepayResultRes> {

    @Override
    public String encode(RepayResultRes object) {

        String s = null;
        try {

            s = JSONObject.toJSONString(object);
            log.info("primal: " + object.toString());

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return s;
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