package com.page.springboot.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Page
 * @Date: 2019-07-02 10:44
 * @Description:
 */

@Data
public class RepayResultRes implements Serializable {
    private static final long serialVersionUID = 6581157052136013425L;
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
    /**
     * 还款结果
     */
    private boolean repayResult;
}
