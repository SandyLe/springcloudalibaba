package org.jeecg.common.util;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;

public class LoginUtils {

    public static LoginUser getLoginUser() {

        return (LoginUser) SecurityUtils.getSubject().getPrincipal();

    }
}
