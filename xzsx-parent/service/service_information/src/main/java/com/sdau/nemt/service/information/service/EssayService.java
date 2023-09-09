package com.sdau.nemt.service.information.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.information.entity.Essay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdau.nemt.service.information.entity.vo.EssayQueryVO;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
public interface EssayService extends IService<Essay> {

    /**
     * 分页条件查询文章
     * @param page
     * @param essayQueryVO
     * @return
     */
    IPage<Essay> findQueryPage(Page<Essay> page, EssayQueryVO essayQueryVO);
}
