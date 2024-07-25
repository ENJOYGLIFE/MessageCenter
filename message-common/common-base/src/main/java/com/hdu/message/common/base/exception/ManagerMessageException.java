package com.hdu.message.common.base.exception;

import com.hdu.message.common.base.enums.error.ErrorEnum;
import lombok.Getter;

/**
 * 消息中心 - 中央消息模块异常
 */
@Getter
public class ManagerMessageException extends RuntimeException {

    /**
     * 用户提示
     */
    private final String userMessage;

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 错误信息
     * 调用成功时，为 null
     * 示例："验证码无效"
     */
    private final String errorMessage;

    public ManagerMessageException(ErrorEnum errorEnum) {
        super(String.format("错误码：[%s]，错误信息：[%s]，用户提示：[%s]", errorEnum.name(), errorEnum.getMessage(), errorEnum.getMessage()));
        this.userMessage = errorEnum.getMessage();
        this.errorCode = errorEnum.name();
        this.errorMessage = errorEnum.getMessage();
    }

    public ManagerMessageException(String userMessage, String errorCode, String errorMessage) {
        super(String.format("错误码：[%s]，错误信息：[%s]，用户提示：[%s]", errorCode, errorMessage, userMessage));
        this.userMessage = userMessage;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
