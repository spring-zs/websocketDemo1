package com.page.springboot.controller;

import com.page.springboot.res.RepayResultRes;
import com.page.springboot.websocket.MoneyServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;

/**
 * @author Page
 * @Date: 2019-07-02 10:36
 * @Description:
 */

@Controller
@RequestMapping("/money")
public class RepaySuccessController {
    @Resource
    MoneyServer moneyServer;

    @PostMapping("/repaySuccess")
    public void repaySuccess(RepayResultRes req) throws IOException, EncodeException {
        moneyServer.send(req);
    }
}
