package org.jeecg.modules.basic.enums;

import java.util.HashMap;
import java.util.Map;

public interface AbstractEnum {

    String getId();

    String getName();

    default Map<String, Object> getMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", this.getName());
        return map;
    }
}
