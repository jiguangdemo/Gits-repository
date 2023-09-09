package com.sdau.nemt.service.volunteer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.volunteer.entity.Specialized;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdau.nemt.service.volunteer.entity.vo.SpecializedQueryVO;

/**
 * <p>
 * 具体专业 服务类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
public interface SpecializedService extends IService<Specialized> {

    /**
     * 条件分页查询专业
     * @param page
     * @param specializedQueryVO
     * @return
     */
    IPage<Specialized> findQueryPage(Page<Specialized> page, SpecializedQueryVO specializedQueryVO);
}
