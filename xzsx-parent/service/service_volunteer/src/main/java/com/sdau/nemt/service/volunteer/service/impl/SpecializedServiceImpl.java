package com.sdau.nemt.service.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.volunteer.entity.Specialized;
import com.sdau.nemt.service.volunteer.entity.vo.SpecializedQueryVO;
import com.sdau.nemt.service.volunteer.mapper.SpecializedMapper;
import com.sdau.nemt.service.volunteer.service.SpecializedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 具体专业 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Service
public class SpecializedServiceImpl extends ServiceImpl<SpecializedMapper, Specialized> implements SpecializedService {

    /**
     * 条件分页查询专业
     * @param page
     * @param specializedQueryVO
     * @return
     */
    @Override
    public IPage<Specialized> findQueryPage(Page<Specialized> page, SpecializedQueryVO specializedQueryVO) {
        LambdaQueryWrapper<Specialized> wrapper = new LambdaQueryWrapper<>();
        //判断条件是否为空，并封装条件
        if (!StringUtils.isEmpty(specializedQueryVO.getName())){
            wrapper.like(Specialized::getName,specializedQueryVO.getName());
        }
        if (!StringUtils.isEmpty(specializedQueryVO.getCode())){
            wrapper.eq(Specialized::getCode,specializedQueryVO.getCode());
        }
        if (!StringUtils.isEmpty(specializedQueryVO.getClassifyId())){
            wrapper.eq(Specialized::getClassifyId,specializedQueryVO.getClassifyId());
        }
        IPage<Specialized> specializedPage = baseMapper.selectPage(page, wrapper);

        return specializedPage;
    }
}
