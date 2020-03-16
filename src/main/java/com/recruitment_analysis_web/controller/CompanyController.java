package com.recruitment_analysis_web.controller;

import com.recruitment_analysis_web.service.CompanyService;
import com.recruitment_analysis_web.util.DataProcessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

//    @CrossOrigin(origins = "http://localhost:8887")
//    @RequestMapping(path = "/getcompanylabellist", method = {RequestMethod.GET})
//    @ResponseBody
//    public String getcompanylabellist(String type, String city) {
//        Map<String, Object> res = companyService.selectCompanyLabelList(type, city);
//        return DataProcessUtil.getJSONString(0, "success", res);
//    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getlabellist", method = {RequestMethod.GET})
    @ResponseBody
    public String getLabelList(String param, String startDate, String endDate) {
        HashMap<String, String> typeMap = new HashMap<String, String>() {{
            put("skill", "技能");
            put("company", "公司优点");
        }};

        List<HashMap<String, Object>> lists = new LinkedList<>();
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            String type = entry.getKey();
            String typeName = entry.getValue();
            Map<String, Object> res = companyService.selectCompanyLabelList(type, param, startDate, endDate);
            lists.add(new LinkedHashMap<String, Object>() {{
                put("name", typeName);
                put("data", res.get("data"));
            }});
        }

        Map<String, Object> res = new HashMap<>();
        res.put("data", lists);

        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getjobrequirement", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE,
            RequestMethod.PATCH, RequestMethod.HEAD, RequestMethod.POST})
    @ResponseBody
    public String getJobRequirement(String type, String param, String startDate, String endDate) {
        Map<String, Object> res = companyService.getJobRequirement(type, param, startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getlocationdata", method = {RequestMethod.GET})
    @ResponseBody
    public String getLocationData(String city, String company_name, String date) {
        Map<String, Object> res = companyService.getLocationData(city, company_name, date);
        return DataProcessUtil.getJSONString(0, "success", res);
    }
}
