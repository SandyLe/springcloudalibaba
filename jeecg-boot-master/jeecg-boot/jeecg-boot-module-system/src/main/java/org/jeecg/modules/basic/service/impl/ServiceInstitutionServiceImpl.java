package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.ServiceInstitution;
import org.jeecg.modules.basic.mapper.ServiceInstitutionMapper;
import org.jeecg.modules.basic.service.ServiceInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceInstitutionServiceImpl extends ServiceImpl<ServiceInstitutionMapper, ServiceInstitution> implements ServiceInstitutionService {

    @Autowired
    private ServiceInstitutionMapper serviceInstitutionMapper;

}
