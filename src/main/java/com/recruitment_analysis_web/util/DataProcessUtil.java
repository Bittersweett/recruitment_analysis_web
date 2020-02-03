package com.recruitment_analysis_web.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class DataProcessUtil {

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String transMapToJSON(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String generateCons(String param, String startDate, String endDate) {
        StringBuilder cons = new StringBuilder();
        String temp = "substring(job_create_ime, 1, 10) between '" + startDate + "' and '" + endDate + "'";
        cons.append(temp);
        JSONObject jsonObject = JSONObject.parseObject(param);
        for (Map.Entry<String, Object> set : jsonObject.entrySet()) {
            String key = set.getKey();
            Object value = set.getValue();
            if (String.valueOf(value).equals("")) {
                continue;
            }
            temp = " and " + key + "='" + value + "'";
            cons.append(temp);
        }
        return String.valueOf(cons);
    }

}
