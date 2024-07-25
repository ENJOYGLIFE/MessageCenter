package com.hdu.message.manager.enums.managermessagetype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerMessageTypeEnum {

    T1(1, "全局通知"),

    T2(2, "栏目订阅"),

    T3(3, "独立消息");

    private final int typeNum;

    private final String message;

}
