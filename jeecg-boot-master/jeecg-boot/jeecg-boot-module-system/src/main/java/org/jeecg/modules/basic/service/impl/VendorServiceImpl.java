package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.Vendor;
import org.jeecg.modules.basic.mapper.VendorMapper;
import org.jeecg.modules.basic.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl extends ServiceImpl<VendorMapper, Vendor> implements VendorService{

    @Autowired
    private VendorMapper vendorMapper;
}
