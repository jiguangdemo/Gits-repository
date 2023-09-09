package com.sdau.nemt.service.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.CollegesDetail;
import com.sdau.nemt.service.volunteer.entity.CollegesIntroduce;
import com.sdau.nemt.service.volunteer.entity.dto.CollegesDetailDTO;
import com.sdau.nemt.service.volunteer.entity.vo.CollegesQueryVO;
import com.sdau.nemt.service.volunteer.mapper.CollegesMapper;
import com.sdau.nemt.service.volunteer.service.CollegesDetailService;
import com.sdau.nemt.service.volunteer.service.CollegesIntroduceService;
import com.sdau.nemt.service.volunteer.service.CollegesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 院校基本信息 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Service
public class CollegesServiceImpl extends ServiceImpl<CollegesMapper, Colleges> implements CollegesService {

    @Autowired
    private CollegesDetailService collegesDetailService;
    @Autowired
    private CollegesIntroduceService collegesIntroduceService;

    /**
     * 按照条件查询
     * @param page
     * @param collegesQueryVO
     * @return
     */
    @Override
    public IPage<Colleges> findQueryPage(Page<Colleges> page, CollegesQueryVO collegesQueryVO) {
        LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
        //获取条件
        String name = collegesQueryVO.getName();
        String province = collegesQueryVO.getProvince();
        Integer type = collegesQueryVO.getType();
        String kind = collegesQueryVO.getKind();
        Integer category = collegesQueryVO.getCategory();
        Integer worldClass = collegesQueryVO.getWorldClass();
        Integer innovation = collegesQueryVO.getInnovation();
        Integer doubleFirstClass = collegesQueryVO.getDoubleFirstClass();
        Integer strongFoundation = collegesQueryVO.getStrongFoundation();
        //判断条件是否为空,封装条件
        if(!StringUtils.isEmpty(name)){
            wrapper.like(Colleges::getName,name);
        }
        if(!StringUtils.isEmpty(province)){
            wrapper.eq(Colleges::getProvince,province);
        }
        if(!StringUtils.isEmpty(type)){
            wrapper.eq(Colleges::getType,type);
        }
        if(!StringUtils.isEmpty(kind)){
            wrapper.eq(Colleges::getKind,kind);
        }
        if(!StringUtils.isEmpty(category)){
            wrapper.eq(Colleges::getCategory,category);
        }
        if(!StringUtils.isEmpty(worldClass)){
            wrapper.eq(Colleges::getWorldClass,worldClass);
        }
        if(!StringUtils.isEmpty(innovation)){
            wrapper.eq(Colleges::getInnovation,innovation);
        }
        if(!StringUtils.isEmpty(doubleFirstClass)){
            wrapper.eq(Colleges::getDoubleFirstClass,doubleFirstClass);
        }
        if(!StringUtils.isEmpty(strongFoundation)){
            wrapper.eq(Colleges::getStrongFoundation,strongFoundation);
        }
        //按照条件查询
        wrapper.orderByAsc(Colleges::getId);
        IPage<Colleges> pageModel = baseMapper.selectPage(page, wrapper);

        return pageModel;
    }

    /**
     * 获取院校详细数据
     * @param id
     * @return
     */
    @Override
    public CollegesDetailDTO getCollegesDetail(String id) {
        //封装前端展示对象
        CollegesDetailDTO collegesDetailDTO = new CollegesDetailDTO();
        LambdaQueryWrapper<Colleges> wrapperColleges = new LambdaQueryWrapper<>();
        wrapperColleges.eq(Colleges::getId,id);
        Colleges colleges = baseMapper.selectOne(wrapperColleges);
        BeanUtils.copyProperties(colleges,collegesDetailDTO);
        LambdaQueryWrapper<CollegesDetail> wrapperDetail = new LambdaQueryWrapper<>();
        wrapperDetail.eq(CollegesDetail::getCollegesId,id);
        CollegesDetail collegesDetail = collegesDetailService.getBaseMapper().selectOne(wrapperDetail);
        BeanUtils.copyProperties(collegesDetail,collegesDetailDTO);
        LambdaQueryWrapper<CollegesIntroduce> wrapperIntroduce = new LambdaQueryWrapper<>();
        wrapperIntroduce.eq(CollegesIntroduce::getCollegesId,id);
        CollegesIntroduce collegesIntroduce = collegesIntroduceService.getBaseMapper().selectOne(wrapperIntroduce);
        BeanUtils.copyProperties(collegesIntroduce,collegesDetailDTO);
        return collegesDetailDTO;
    }
}
