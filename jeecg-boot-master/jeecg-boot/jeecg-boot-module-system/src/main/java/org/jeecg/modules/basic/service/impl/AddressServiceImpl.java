package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.basic.entity.Address;
import org.jeecg.modules.basic.mapper.AddressMapper;
import org.jeecg.modules.basic.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService{

    @Autowired
    private AddressMapper addressMapper;
}
