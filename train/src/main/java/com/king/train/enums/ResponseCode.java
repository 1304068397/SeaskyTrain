package com.king.train.enums;

/**
 * @PackageName:com.king.train.enums
 * @ClassName:ResponseCode
 * @Description:
 * @Author:王宗保
 * @Version V1.0.0
 * @Date:2020/11/8 22:19
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS("200", "成功"),
    /**
     * 失败
     */
    FAILURE("99999", "失败"),

    /**
     * 未登录
     */
    LOGIN_NOT_ONLINE("10000", "未登录"),
    /**
     * 登录异常
     */
    LOGIN_EXCEPTION("10001", "登录异常"),

    ;

    private final String value;
    private final String message;

    ResponseCode(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String value() {
        return this.value;
    }

    public String message() {
        return message;
        //return Resources.getMessage("RESPONSECODE_" + this.value);
    }

    @Override
    public String toString() {
        return this.name() + "" + this.value.toString();
    }

}
