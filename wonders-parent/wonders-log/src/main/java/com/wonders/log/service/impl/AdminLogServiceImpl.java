package com.wonders.log.service.impl;

import com.wonders.log.dao.AdminLogDao;
import com.wonders.log.entity.AdminLog;
import com.wonders.log.service.AdminLogService;
import com.wonders.mybatis.page.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 通用日志标(SSealCurrentLog)表服务实现类
 *
 * @author wuxingxing
 * @since 2021-03-07 12:48:36
 */
@Service("adminLogService")
public class AdminLogServiceImpl implements AdminLogService {
    @Resource
    private AdminLogDao adminLogDao;


    @Override
    public List<AdminLog> getList(Map<String, Object> map) {
        return null;
    }

    @Override
    public PageBean<?> getPageList(Map<String, Object> map) {
        return null;
    }

    @Override
    public AdminLog getInfoById(Object obj) {
        return null;
    }

    @Override
    public int save(AdminLog obj) {
        obj.init();
        return this.adminLogDao.save(obj);
    }

    @Override
    public int update(AdminLog obj) {
        return 0;
    }

    @Override
    public int deleteById(Object obj) {
        return 0;
    }

    @Override
    public int deleteByIds(Object[] obj) {
        return 0;
    }
}