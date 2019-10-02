package com.page.springboot.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Page
 * @Date: 2019-07-03 16:35
 * @Description:
 */

@Data
public class RepayReq implements Serializable {
    private static final long serialVersionUID = -7662558345703396190L;
    /**
     * 用户号
     */
    private String userId;
    /**
     * 还款人名
     */
    private String name;
    /**
     * 还款金额
     */
    private int moneyNum;
}
