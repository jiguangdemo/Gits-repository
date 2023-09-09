package com.sdau.nemt.service.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecializedUsual;
import com.sdau.nemt.service.volunteer.entity.dto.CollegesProbabilityDTO;
import com.sdau.nemt.service.volunteer.entity.dto.MockDTO;
import com.sdau.nemt.service.volunteer.entity.dto.SpecializedProbabilityDTO;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.entity.vo.MockCollegesQueryVO;
import com.sdau.nemt.service.volunteer.mapper.CollegesMapper;
import com.sdau.nemt.service.volunteer.mapper.CollegesSpecializedMapper;
import com.sdau.nemt.service.volunteer.service.CollegesSpecializedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdau.nemt.service.volunteer.service.CollegesSpecializedUsualService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 院校专业 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Service
public class CollegesSpecializedServiceImpl extends ServiceImpl<CollegesSpecializedMapper, CollegesSpecialized> implements CollegesSpecializedService {

    @Autowired
    private CollegesSpecializedUsualService collegesSpecializedUsualService;

    @Autowired
    private CollegesMapper collegesMapper;

    /**
     * 将概率存入院校
     * @param infoVO
     * @param dtoList
     * @return
     */
    @Override
    public List<CollegesProbabilityDTO> addProbability(InfoVO infoVO, List<CollegesProbabilityDTO> dtoList) {
        String scores = infoVO.getScores();
        String bits = infoVO.getBits();
        List<String> listId = new ArrayList<>();
        for (int i = 0; i < dtoList.size(); i++) {
            listId.add(dtoList.get(i).getId());
        }
        List<CollegesSpecializedUsual> collegesSpecializedUsuals = collegesSpecializedUsualService.getBaseMapper().selectBatchIds(listId);
        Double probability = 0.0;
        Double count = 0.0;
        for (int i = 0; i <collegesSpecializedUsuals.size(); i++) {
            if (collegesSpecializedUsuals.get(i).getLastYearAverageScore()!=null) {
                if (Integer.parseInt(scores) >= Integer.parseInt(collegesSpecializedUsuals.get(i).getLastYearAverageScore())) {
                    probability = probability + 0.2375;
                }
            }
            if (collegesSpecializedUsuals.get(i).getTwoYearsAverageScore()!=null) {
                if (Integer.parseInt(scores) >=Integer.parseInt(collegesSpecializedUsuals.get(i).getTwoYearsAverageScore())){
                    probability = probability + 0.2375;
                }
            }
            if (collegesSpecializedUsuals.get(i).getLastYearAverageBit()!=null) {
                if (Integer.parseInt(bits) <=Integer.parseInt(collegesSpecializedUsuals.get(i).getLastYearAverageBit())){
                    probability = probability + 0.2375;
                }
            }
            if (collegesSpecializedUsuals.get(i).getTwoYearsAverageBit()!=null) {
                if (Integer.parseInt(bits) <=Integer.parseInt(collegesSpecializedUsuals.get(i).getTwoYearsAverageBit())){
                    probability = probability + 0.2375;
                }
            }
            if (probability >= 0.5){
                count = count + 1;
            }
            String odds = "";
            if(count/collegesSpecializedUsuals.size() > 0.75 ){
                odds = "概率大";
            }else if (count/collegesSpecializedUsuals.size() >= 0.5 ){
                odds = "概率中";
            }else {
                odds = "概率小";
            }
            dtoList.get(i).setProbability(odds);
            probability = 0.0;
            count = 0.0;
        }
        return dtoList;
    }

    /**
     * 专业录取概率
     * @param infoVO
     * @param collegesSpecializedList
     * @return
     */
    @Override
    public List<SpecializedProbabilityDTO> specializedProbability(InfoVO infoVO, List<CollegesSpecialized> collegesSpecializedList) {
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < collegesSpecializedList.size(); i++) {
            idList.add(collegesSpecializedList.get(i).getId());
        }
        List<SpecializedProbabilityDTO> model = new ArrayList<>();
        List<CollegesSpecializedUsual> collegesSpecializedUsualList = collegesSpecializedUsualService.getBaseMapper().selectBatchIds(idList);
        for (int i = 0; i < collegesSpecializedUsualList.size(); i++) {
            SpecializedProbabilityDTO specializedProbabilityDTO = new SpecializedProbabilityDTO();
            BeanUtils.copyProperties(collegesSpecializedUsualList.get(i),specializedProbabilityDTO);
            BeanUtils.copyProperties(collegesSpecializedList.get(i),specializedProbabilityDTO);
            model.add(specializedProbabilityDTO);
        }
//        for (int i = 0; i < collegesSpecializedList.size(); i++) {
//            model.add(specializedProbabilityDTO);
//        }
        String scores = infoVO.getScores();
        String bits = infoVO.getBits();
        String probability = "";
        Double odds = 0.0;

        if (model==null&&model.equals("")){
            return model;
        }
        for (int i = 0; i < model.size(); i++) {
            if(model.get(i).getLastYearAverageScore()!=null){
                if (Integer.parseInt(scores)>=Integer.parseInt(model.get(i).getLastYearAverageScore())){
                    odds = odds + 0.2375;
                }
            }
            if (model.get(i).getLastYearAverageBit()!=null){
                if (Integer.parseInt(bits)<=Integer.parseInt(model.get(i).getLastYearAverageBit())){
                    odds = odds + 0.2375;
                }
            }
            if (model.get(i).getTwoYearsAverageScore()!=null){
                if (Integer.parseInt(scores)>=Integer.parseInt(model.get(i).getTwoYearsAverageScore())){
                    odds = odds +0.2375;
                }
            }
            if (model.get(i).getTwoYearsAverageBit()!=null){
                if (Integer.parseInt(bits)<=Integer.parseInt(model.get(i).getTwoYearsAverageBit())){
                    odds = odds + 0.2375;
                }
            }
            if (odds>=0.9){
                probability = "95%";
            }else if (odds>=0.7){
                probability = "75%";
            }else if (odds>=0.4){
                probability = "50%";
            }else if (odds>=0.2){
                probability = "25%";
            }else {
                probability = "1%";
            }
            model.get(i).setProbability(probability);
            odds = 0.0;
            probability = "";
        }
        return model;
    }

    /**
     * 查询志愿志愿列表
     * @param infoVO
     * @param mockQueryVO
     */
    @Override
    public void getCollegesMock(InfoVO infoVO, MockCollegesQueryVO mockQueryVO) {

        String name = mockQueryVO.getName();
        String province = mockQueryVO.getProvince();
        String city = mockQueryVO.getCity();
        String kind = mockQueryVO.getKind();
        Integer category = mockQueryVO.getCategory();
        Integer worldClass = mockQueryVO.getWorldClass();
        Integer innovation = mockQueryVO.getInnovation();
        Integer doubleFirstClass = mockQueryVO.getDoubleFirstClass();
//        String phylum = mockQueryVO.getPhylum();
//        Integer type = mockQueryVO.getType();
//        String specialized = mockQueryVO.getSpecialized();
        Integer pick = mockQueryVO.getPick();
        LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
        if (name!=null && !name.equals("")){
            wrapper.like(Colleges::getName,name);
        }
        if (province!=null && !province.equals("")){
            wrapper.eq(Colleges::getProvince,province);
        }
        if (city!=null && !city.equals("")){
            wrapper.eq(Colleges::getCity,city);
        }
        if (kind!=null && !kind.equals("")){
            wrapper.eq(Colleges::getKind,kind);
        }
        if (category!=null && !category.equals("")){
            wrapper.eq(Colleges::getCategory,category);
        }
        if (worldClass!=null && !worldClass.equals("")){
            wrapper.eq(Colleges::getWorldClass,worldClass);
        }
        if (innovation!=null && !innovation.equals("")){
            wrapper.eq(Colleges::getInnovation,innovation);
        }
        if (doubleFirstClass!=null && !doubleFirstClass.equals("")){
            wrapper.eq(Colleges::getDoubleFirstClass,doubleFirstClass);
        }
        List<Colleges> collegesList = collegesMapper.selectList(wrapper);

        List<String> idList = new ArrayList<>();
        for (int i = 0; i < collegesList.size(); i++) {
            idList.add(collegesList.get(i).getId());
        }
        for (int i = 0; i < collegesList.size(); i++) {
            MockDTO mockDTO = new MockDTO();
            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CollegesSpecialized::getCollegesId,collegesList.get(i).getId());
            List<CollegesSpecialized> collegesSpecializedList = baseMapper.selectList(queryWrapper);

        }

    }

}
