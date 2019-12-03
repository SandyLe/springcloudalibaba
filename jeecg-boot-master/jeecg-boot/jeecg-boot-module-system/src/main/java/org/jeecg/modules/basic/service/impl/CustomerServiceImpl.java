package org.jeecg.modules.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.basic.dto.CustomerEditDto;
import org.jeecg.modules.basic.entity.Customer;
import org.jeecg.modules.basic.entity.CustomerDeliveryInfo;
import org.jeecg.modules.basic.mapper.CustomerMapper;
import org.jeecg.modules.basic.service.CustomerDeliveryInfoService;
import org.jeecg.modules.basic.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService{

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerDeliveryInfoService customerDeliveryInfoService;

    public String save(CustomerEditDto dto){
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto, customer);
        save(customer);

        CustomerDeliveryInfo cdi = new CustomerDeliveryInfo();
        BeanUtils.copyProperties(dto, cdi, "name", "code");
        if(StringUtils.isBlank(dto.getCdi_sourceId())){
            cdi.setCdi_sourceId(customer.getId());
        }
        cdi.setId(dto.getCdi_id());
        customerDeliveryInfoService.save(cdi);
        return customer.getId();
    }
}
