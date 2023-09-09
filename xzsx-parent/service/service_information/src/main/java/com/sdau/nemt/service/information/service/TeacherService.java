package com.sdau.nemt.service.information.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.information.entity.Essay;
import com.sdau.nemt.service.information.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdau.nemt.service.information.entity.dto.TeacherDTO;
import com.sdau.nemt.service.information.entity.vo.TeacherVO;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
public interface TeacherService extends IService<Teacher> {

    /**
     *分页条件查询咨询师
     * @param page
     * @param teacherVO
     * @return
     */
    IPage<TeacherDTO> findQueryPage(Page<Teacher> page, TeacherVO teacherVO);
}
