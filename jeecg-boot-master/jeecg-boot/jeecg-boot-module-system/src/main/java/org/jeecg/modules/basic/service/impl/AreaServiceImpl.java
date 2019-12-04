package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.Area;
import org.jeecg.modules.basic.mapper.AreaMapper;
import org.jeecg.modules.basic.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{

    @Autowired
    private AreaMapper areaMapper;
}
