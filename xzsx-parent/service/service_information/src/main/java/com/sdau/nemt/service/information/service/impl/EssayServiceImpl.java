package com.sdau.nemt.service.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.information.entity.Essay;
import com.sdau.nemt.service.information.entity.vo.EssayQueryVO;
import com.sdau.nemt.service.information.mapper.EssayMapper;
import com.sdau.nemt.service.information.service.EssayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
@Service
public class EssayServiceImpl extends ServiceImpl<EssayMapper, Essay> implements EssayService {

    /**
     * 分页条件查询文章
     * @param page
     * @param essayQueryVO
     * @return
     */
    @Override
    public IPage<Essay> findQueryPage(Page<Essay> page, EssayQueryVO essayQueryVO) {
        //判断条件，封装条件
        String title = essayQueryVO.getTitle();
        String author = essayQueryVO.getAuthor();
        String typeId = essayQueryVO.getTypeId();
        LambdaQueryWrapper<Essay> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(title)){
            wrapper.like(Essay::getTitle,title);
        }
        if(!StringUtils.isEmpty(author)){
            wrapper.like(Essay::getAuthor,author);
        }
        if(!StringUtils.isEmpty(typeId)){
            wrapper.eq(Essay::getTypeId,typeId);
        }
        IPage<Essay> essayPage = baseMapper.selectPage(page, wrapper);
        return essayPage;
    }
}
