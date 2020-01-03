package org.jeecg.modules.basic.enums;

import java.util.HashMap;
import java.util.Map;

public interface AbstractEnum {

    String getSid();

    String getName();

    Object getId();

    default Map<String, Object> getMap() {

        Map<String, Object> map = new HashMap<>();
        try {
            if (getId() instanceof Integer) {
                int id = Integer.parseInt(getSid());
                map.put("id", id);
            } else {
                map.put("id", getSid());
            }
        }
        catch (NumberFormatException e) {
            map.put("id", getSid());
        }
        map.put("name", this.getName());
        return map;
    }
}
