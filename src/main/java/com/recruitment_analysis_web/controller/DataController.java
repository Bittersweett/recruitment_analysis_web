package com.recruitment_analysis_web.controller;

import com.alibaba.fastjson.JSONObject;
import com.recruitment_analysis_web.entity.JobMessage;
import com.recruitment_analysis_web.service.DataService;
import com.recruitment_analysis_web.util.DataProcessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/data", method = {RequestMethod.GET})
    @ResponseBody
    public String getData() {
        Map<String, Object> res = dataService.selectCityDemand();
        System.out.println(res);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getcitydemandofsecondtype", method = {RequestMethod.GET})
    @ResponseBody
    public String getCityDemandOfSecondType() {
        Map<String, Object> res = dataService.selectCityDemandOfSecondType();
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getcntoffinancestage", method = {RequestMethod.GET})
    @ResponseBody
    public String getCntOfFinanceStage(String city, String startDate, String endDate) {
        Map<String, Object> res = dataService.selectCntOfFinanceStage(city, startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getsunburstdata", method = {RequestMethod.GET})
    @ResponseBody
    public String getSunburstData(String startDate, String endDate) {
        Map<String, Object> res = dataService.getSunburstData(startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getresumecountofcitybydate", method = {RequestMethod.GET})
    @ResponseBody
    public String getResumeCountOfCityBydate(String startDate, String endDate) {
        Map<String, Object> res = dataService.getResumeCountOfCityByDate(startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getresumecountofcity", method = {RequestMethod.GET})
    @ResponseBody
    public String getResumeCountOfCity(String startDate, String endDate) {
        Map<String, Object> res = dataService.getResumeCountOfCity(startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getrankdata", method = {RequestMethod.GET})
    @ResponseBody
    public String getRankData(String type) {
        Map<String, Object> res = dataService.getRankData(type);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getoptions", method = {RequestMethod.GET})
    @ResponseBody
    public String getOptions(String type) {
        Map<String, Object> res = dataService.getOptions(type);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/gettrafficdata", method = {RequestMethod.GET})
    @ResponseBody
    public String getTrafficData(String type, String city, String startDate, String endDate) {
        Map<String, Object> res = dataService.getTrafficData(type, city, startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }


    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getbusinesszonesdata", method = {RequestMethod.GET})
    @ResponseBody
    public String getBusinessZonesData(String city, String startDate, String endDate) {
        Map<String, Object> res = dataService.getBusinessZonesData("business_zones", city, startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }

    @CrossOrigin(origins = "http://localhost:8887")
    @RequestMapping(path = "/getavgsalary", method = {RequestMethod.GET})
    @ResponseBody
    public String getAvgSalary(String param, String startDate, String endDate) {

        Map<String, Object> res = dataService.getAvgSalary(param, startDate, endDate);
        return DataProcessUtil.getJSONString(0, "success", res);
    }
}
