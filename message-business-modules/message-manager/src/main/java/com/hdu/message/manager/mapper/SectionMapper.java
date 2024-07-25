package com.hdu.message.manager.mapper;

import com.hdu.message.common.base.entity.Section;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Section)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-20 16:12:27
 */
@Repository
public interface SectionMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Section queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param section 查询条件
     * @return 对象列表
     */
    List<Section> queryAllByLimit(Section section);

    /**
     * 统计总行数
     *
     * @param section 查询条件
     * @return 总行数
     */
    long count(Section section);

    /**
     * 新增数据
     *
     * @param section 实例对象
     * @return 影响行数
     */
    int insert(Section section);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Section> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Section> entities);

    /**
     * 修改数据
     *
     * @param section 实例对象
     * @return 影响行数
     */
    int update(Section section);

}

