package com.sdau.nemt.service.volunteer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdau.nemt.service.volunteer.entity.dto.CollegesProbabilityDTO;
import com.sdau.nemt.service.volunteer.entity.dto.SpecializedProbabilityDTO;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.entity.vo.MockCollegesQueryVO;

import java.util.List;

/**
 * <p>
 * 院校专业 服务类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
public interface CollegesSpecializedService extends IService<CollegesSpecialized> {

    /**
     * 院校录取概率
     * @param infoVO
     * @param dtoList
     * @return
     */
    List<CollegesProbabilityDTO> addProbability(InfoVO infoVO, List<CollegesProbabilityDTO> dtoList);

    /**
     * 专业录取概率
     * @param infoVO
     * @param collegesSpecializedList
     * @return
     */
    List<SpecializedProbabilityDTO> specializedProbability(InfoVO infoVO, List<CollegesSpecialized> collegesSpecializedList);

    /**
     * 按照院校条件查询志愿志愿列表
     * @param infoVO
     * @param mockQueryVO
     */
    void getCollegesMock(InfoVO infoVO, MockCollegesQueryVO mockQueryVO);
}
