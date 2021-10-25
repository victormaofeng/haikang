package top.maof.haikang.utils;

import java.util.Map;

@Deprecated
public class QueryUtil {
    public static Map parse(Map map) {
        if (map.get("start") != null) {
            map.put("start", Integer.parseInt(map.get("start") + ""));
            map.put("pageSize", Integer.parseInt(map.get("pageSize") + ""));
        }
        return map;
    }
}
