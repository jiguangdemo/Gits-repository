package com.sdau.nemt.service.volunteer.service.impl;

import com.sdau.nemt.service.volunteer.entity.AATable;
import com.sdau.nemt.service.volunteer.mapper.AATableMapper;
import com.sdau.nemt.service.volunteer.service.AATableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 2023年分数对应最低位次，用于判断学生位次 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Service
public class AATableServiceImpl extends ServiceImpl<AATableMapper, AATable> implements AATableService {

}
