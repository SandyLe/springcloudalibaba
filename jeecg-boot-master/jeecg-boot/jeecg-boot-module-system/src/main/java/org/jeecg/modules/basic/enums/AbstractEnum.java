package org.jeecg.modules.basic.enums;

import java.util.HashMap;
import java.util.Map;

public interface AbstractEnum {

    String getSid();

    String getName();

    default Map<String, Object> getMap() {

        Map<String, Object> map = new HashMap<>();
        try {
            int id = Integer.parseInt(getSid());
            map.put("id", id);
        }
        catch (NumberFormatException e) {
            map.put("id", getSid());
        }
        map.put("name", this.getName());
        return map;
    }
}
