package org.jeecg.common.system.api;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionDataRule;

import java.util.List;

public interface ISysPermissionAPI extends IService<SysPermission> {

    public List<SysPermissionDataRule> queryPermissionDataRules(String username, List<String> sysPermission);
}
