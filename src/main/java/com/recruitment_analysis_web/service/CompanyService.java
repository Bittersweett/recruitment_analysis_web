package com.recruitment_analysis_web.service;

import com.recruitment_analysis_web.dao.DataMapper;
import com.recruitment_analysis_web.entity.JobMessage;
import com.recruitment_analysis_web.util.DataProcessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {

    @Autowired
    private DataMapper dataMapper;

    public Map<String, Object> selectCompanyLabelList(String type, String params, String startDate, String endDate) {
        List<JobMessage> lists = new LinkedList<>();
        String cons = DataProcessUtil.generateCons(params, startDate, endDate);
        switch (type) {
            case "skill":
                lists = dataMapper.selectSkillLabelList(cons);
                break;
            case "company":
                lists = dataMapper.selectCompanyLabelList(cons);
                break;
            case "position":
                lists = dataMapper.selectPositionAdvantageList(cons);
                break;
        }
        System.out.println(lists);

        Map<String, Integer> labelMap = new HashMap<>();
        for (JobMessage list : lists) {
            String labels = "";
            switch (type) {
                case "skill":
                    labels = list.getSkillLables();
                    break;
                case "company":
                    labels = list.getCompanyLabelList();
                    break;
                case "position":
                    labels = list.getPositionAdvantage();
                    break;
            }

            String[] labelArrs = labels.split("\\|");

            for (String cur : labelArrs) {
                if (cur.length() > 0) {
                    labelMap.put(cur, labelMap.getOrDefault(cur, 0) + 1);
                }
            }
        }

        Map<String, Integer> sortLabelMap = new LinkedHashMap<>();
        labelMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortLabelMap.put(x.getKey(), x.getValue()));


        System.out.println(sortLabelMap);
        Map<String, Object> res = new HashMap<>();

        List<HashMap<String, Object>> listOfLabel = new LinkedList<>();
        int cnt = 20;
        int index = 0;
        for (Map.Entry<String, Integer> entry : sortLabelMap.entrySet()) {
            String label = entry.getKey();
            int frequencyOfLabel = entry.getValue();
            //int curIndex = index + 1;

            listOfLabel.add(new HashMap<String, Object>() {{
                //put("id", curIndex);
                put("name", label);
                put("weight", frequencyOfLabel);
            }});

            if (++index >= cnt) {
                break;
            }
        }

        res.put("data", listOfLabel);
        return res;
    }


    public Map<String, Object> getJobRequirement(String type, String param, String startDate, String endDate) {

        String cons = DataProcessUtil.generateCons(param, startDate, endDate);
        System.out.println("cons    " +  cons);
        List<JobMessage> lists = dataMapper.selectDistinctFieldOfParams(type, cons);

        List<String> categories = new LinkedList<>();
        List<Map<String, Object>> seriesDataList = new LinkedList<>();
        Map<String, Object> seriesData = new LinkedHashMap<>();
        List<Integer> seriesItem = new LinkedList<>();
        for (JobMessage list : lists) {
            switch (type) {
                case "work_year":
                    categories.add(list.getWorkYear());
                    break;
                case "education":
                    categories.add(list.getEducation());
                    break;
            }
            seriesItem.add(list.getCnt());
        }

        switch (type) {
            case "work_year":
                seriesData.put("name", "工作年限");
                break;
            case "education":
                seriesData.put("name", "学历");
                break;
        }
        seriesData.put("data", seriesItem);
        seriesDataList.add(seriesData);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("series", seriesDataList);
        dataMap.put("categories", categories);

        Map<String, Object> res = new HashMap<>();
        res.put("data", dataMap);
        return res;
    }

    public Map<String, Object> getLocationData(String city, String company_name, String date) {
        List<JobMessage> lists = dataMapper.selectLocationData(city, company_name, date);

        List<Map<String, Object>> dataList = new LinkedList<>();
        for (JobMessage list : lists) {
            if (list.getLatitude() == null || list.getLongitude() == null) {
                continue;
            }
            String lat = list.getLatitude();
            String lon = list.getLongitude();
            String companyName = list.getCompanyName();
            String companyShortName = list.getCompnayShortName();

            List<Double> locationData = new LinkedList<Double>() {{
                add(Double.parseDouble(lon));
                add(Double.parseDouble(lat));
            }};

            dataList.add(new LinkedHashMap<String, Object>() {{
                put("location", locationData);
                put("company_name", companyName);
                put("company_short_name", companyShortName);
            }});
        }

        Map<String, Object> res = new HashMap<>();
        res.put("data", dataList);
        return res;
    }
}
