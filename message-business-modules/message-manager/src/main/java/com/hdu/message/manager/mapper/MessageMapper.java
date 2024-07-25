package com.hdu.message.manager.mapper;

import com.hdu.message.manager.bean.ManagerNotice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    // 新增
    Integer insert(ManagerNotice managerNotice);

    // 批量新增
    Integer insertBatch(List<ManagerNotice> managerNoticeList);

    // 查询单条记录
    ManagerNotice queryById(String id);

    // 查询条件管理员消息
    List<ManagerNotice> queryAllByLimit(@Param("managerNotice") ManagerNotice managerNotice, @Param("sort") String sort, @Param("order") String order);

    // 统计总条数
    Long count(ManagerNotice managerNotice);

    // 更新消息
    Integer update(ManagerNotice managerNotice);
}
