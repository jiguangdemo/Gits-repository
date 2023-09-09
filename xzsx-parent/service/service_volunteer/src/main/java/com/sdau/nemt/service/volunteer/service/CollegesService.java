package com.sdau.nemt.service.volunteer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdau.nemt.service.volunteer.entity.dto.CollegesDetailDTO;
import com.sdau.nemt.service.volunteer.entity.vo.CollegesQueryVO;

/**
 * <p>
 * 院校基本信息 服务类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
public interface CollegesService extends IService<Colleges> {

    /**
     * 按照条件查询
     * @param page
     * @param collegesQueryVO
     * @return
     */
    IPage<Colleges> findQueryPage(Page<Colleges> page, CollegesQueryVO collegesQueryVO);

    /**
     * 获取院校详细数据
     * @param id
     * @return
     */
    CollegesDetailDTO getCollegesDetail(String id);
}
