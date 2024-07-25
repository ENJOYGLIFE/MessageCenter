package com.hdu.message.common.base.enums.redismessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisMessageKeyEnum {

    M1("ManagerAllMessage", 1,"全局通知信息"),
    M2("Section-2", 2, "栏目2"),
    M3("Section-3", 3, "栏目3");

    private final String messageKeyType;

    private final Integer sectionId;

    private final String message;

    /**
     * 根据板块号获取对应Redis的key
     * @param sectionId 板块号
     * @return Enum
     */
    public static RedisMessageKeyEnum getEnumBySectionId(Integer sectionId) {
        for (RedisMessageKeyEnum key : RedisMessageKeyEnum.values()) {
            if (key.getSectionId().equals(sectionId)) {
                return key;
            }
        }
        return null; // 或者抛出异常，例如 IllegalArgumentException("无效的 sectionId: " + sectionId);
    }
}
