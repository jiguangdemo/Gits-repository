package com.sdau.nemt.service.information.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.service.information.entity.Essay;
import com.sdau.nemt.service.information.entity.Teacher;
import com.sdau.nemt.service.information.entity.dto.TeacherDTO;
import com.sdau.nemt.service.information.entity.vo.TeacherVO;
import com.sdau.nemt.service.information.mapper.TeacherMapper;
import com.sdau.nemt.service.information.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    /**
     * 分页条件查询咨询师
     * @param page
     * @param teacherVO
     * @return
     */
    @Override
    public IPage<TeacherDTO> findQueryPage(Page<Teacher> page, TeacherVO teacherVO) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        String name = teacherVO.getName();
        Integer level = teacherVO.getLevel();
        Integer sex = teacherVO.getSex();
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.like("level",level);
        }
        if(!StringUtils.isEmpty(sex)){
            wrapper.like("sex",sex);
        }
        IPage<Teacher> teacherPage = baseMapper.selectPage(page, wrapper);
        List<TeacherDTO> list = new ArrayList<>();
        List<Teacher> records = teacherPage.getRecords();
        for(int i =0 ;i<records.size(); i++){
            TeacherDTO teacherDTO = new TeacherDTO();
            BeanUtils.copyProperties(records.get(i),teacherDTO);
            list.add(teacherDTO);
        }
        IPage<TeacherDTO> teacherDTOIPage = new Page<>();
        BeanUtils.copyProperties(teacherPage,teacherDTOIPage);
        teacherDTOIPage.setRecords(list);
        return teacherDTOIPage;
    }
}
