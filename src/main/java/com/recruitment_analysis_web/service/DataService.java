package com.recruitment_analysis_web.service;

import com.alibaba.fastjson.JSONObject;
import com.recruitment_analysis_web.dao.DataMapper;
import com.recruitment_analysis_web.entity.JobMessage;
import com.recruitment_analysis_web.util.DataProcessUtil;
import com.recruitment_analysis_web.util.RevenueProcessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataService {

    @Autowired
    private DataMapper dataMapper;

    private List<String> cityList = new LinkedList<String>() {{
        add("北京");
        add("深圳");
        add("上海");
        add("广州");
//        add("杭州");
    }};

    public Map<String, Object> selectCityDemand() {

        List<JobMessage> lists = dataMapper.selectCityDemand(); // 获取每个城市的需求量

        LinkedList<String> cityList = new LinkedList<>();

        Map<String, Integer> demapMap = new LinkedHashMap<>(); // 城市-需求 集合

        int numOfCity = 0; // 城市数
        for (JobMessage list : lists) {
            int curCnt = list.getCnt();
            String curCity = curCnt < 1000 ? "其他" : list.getCity();

            if (!cityList.contains(curCity)) {
                cityList.add(curCity);
            }

            demapMap.put(curCity, demapMap.getOrDefault(curCity, 0) + curCnt);
        }

        Map<String, Object> xAxisMap = new LinkedHashMap<String, Object>() {{
            put("categories", cityList);
        }};

        List<Object> seriesList = new LinkedList<>();
        List<Integer> tempList = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : demapMap.entrySet()) {
            tempList.add(entry.getValue());
        }


        seriesList.add(new LinkedHashMap<String, Object>() {{
            put("name", "岗位需求量");
            put("data", tempList);
        }});


        // 最终结果
        Map<String, Object> res = new LinkedHashMap<String, Object>() {{
            put("data", new LinkedHashMap<String, Object>() {{
                put("xAxis", xAxisMap);
                put("series", seriesList);
            }});
        }};

        return res;
    }

    public Map<String, Object> selectCityDemandOfSecondType() {
        List<JobMessage> lists = dataMapper.selectCityDemandOfSecondType();

        Map<String, Object> res = new LinkedHashMap<>();
        return res;
    }

    public Map<String, Object> selectCntOfFinanceStage(String param, String startDate, String endDate) {
        String cons = DataProcessUtil.generateCons(param, startDate, endDate);
        System.out.println("ccons " + cons);
        List<JobMessage> lists = dataMapper.selectCntOfFinanceStage(cons);

        List<String> financeStages = new LinkedList<String>() {{
            add("A轮");
            add("B轮");
            add("C轮");
            add("D轮及以上");
            add("上市公司");
            add("不需要融资");
            add("天使轮");
            add("未融资");
        }};

        List<String> districtList = new LinkedList<>();
        Map<String, HashMap<String, Integer>> cntOfFinanceStageMap = new HashMap<>();
        for (JobMessage list : lists) {
            if (!districtList.contains(list.getDistrict())) {
                districtList.add(list.getDistrict());
            }
            if (!cntOfFinanceStageMap.containsKey(list.getFinanceStage())) {
                cntOfFinanceStageMap.put(list.getFinanceStage(), new HashMap<String, Integer>() {{
                    put(list.getDistrict(), list.getCnt());
                }});
            } else {
                cntOfFinanceStageMap.get(list.getFinanceStage()).put(list.getDistrict(), list.getCnt());
            }
        }

        List<Object> seriesList = new LinkedList<>();
        for (String district : districtList) {

            List<Integer> dataList = new LinkedList<>();
            for (String financeStage : financeStages) {
                if (cntOfFinanceStageMap.get(financeStage) != null && cntOfFinanceStageMap.get(financeStage).get(district) != null) {
                    dataList.add(cntOfFinanceStageMap.get(financeStage).get(district));
                } else {
                    dataList.add(0);

                }
            }
            System.out.println(dataList);
            seriesList.add(new LinkedHashMap<String, Object>() {{
                put("name", district);
                put("data", dataList);
            }});
        }


        // 计算各融资阶段的需求
        List<JobMessage> financeStageCnt = dataMapper.selectDistinctFieldOfParams("finance_stage", cons);

        int total = 0;
        Map<String, Integer> fananceStageDemandMap = new HashMap<>();
        for (JobMessage jobMessage : financeStageCnt) {
            int cnt = jobMessage.getCnt();
            String financeStage = jobMessage.getFinanceStage();

            fananceStageDemandMap.put(financeStage, cnt);
            total += cnt;
        }

        List<LinkedList<Object>> pieData = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : fananceStageDemandMap.entrySet()) {
            int cnt = entry.getValue();
            String financeStage = entry.getKey();
            float ratio = (float) (cnt * 100) / total;

            pieData.add(new LinkedList<Object>() {{
                add(financeStage + " " + cnt);
                add(Float.parseFloat(String.format("%.2f", ratio)));
            }});
        }


        // 最终结果
        Map<String, Object> res = new LinkedHashMap<String, Object>() {{
            put("data", new LinkedHashMap<String, Object>() {{
                put("xAxis", financeStages);
                put("series", seriesList);
                put("pie", pieData);
            }});
        }};
        return res;
    }


    public Map<String, Object> getSunburstData(String startDate, String endDate) {
        List<JobMessage> lists = dataMapper.selectCityDistrictDemand(startDate, endDate);

        // 生成区域->城市的映射表
        Map<String, String> districtCityMap = new HashMap<>();

        // 生成区域->需求的映射表
        Map<String, Integer> districtDemandMap = new HashMap<>();

        // 城市集合
        HashSet<String> citySet = new HashSet<>();

        for (JobMessage list : lists) {
            String district = list.getDistrict();
            String city = list.getCity();
            int demand = list.getCnt();
            citySet.add(city);
            if (!districtCityMap.containsKey(district)) {
                districtCityMap.put(district, city);
            }
            districtDemandMap.put(district, demand);
        }

        List<HashMap<String, Object>> listOfSunburstData = new LinkedList<>();
        listOfSunburstData.add(new HashMap<String, Object>() {{
            put("id", "0.0");
            put("parent", "");
            put("name", "All");
        }});

        for (String city : citySet) {
            listOfSunburstData.add(new HashMap<String, Object>() {{
                put("id", city);
                put("parent", "0.0");
                put("name", city);
            }});
        }

        for (Map.Entry<String, String> entry : districtCityMap.entrySet()) {
            String district = entry.getKey();
            String city = entry.getValue();
            listOfSunburstData.add(new HashMap<String, Object>() {{
                put("id", district);
                put("parent", city);
                put("name", district);
                put("value", districtDemandMap.get(district));
            }});
        }

        System.out.println(listOfSunburstData);

        Map<String, Object> res = new HashMap<>();
        res.put("data", listOfSunburstData);
        return res;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<String, Object> getResumeCountOfCityByDate(String startDate, String endDate) {
        List<JobMessage> lists = dataMapper.selectResumeCountOfCityByDate(startDate, endDate);
        System.out.println(lists);


        // 日期集合
        TreeSet<String> dateSet = new TreeSet<>();

        // 计算 日期-其他城市的简历数量
        Map<String, Integer> otherCityDemandMap = new HashMap<>();
        for (JobMessage jobMessage : lists) {
            int cnt = jobMessage.getCnt();
            String city = jobMessage.getCity();
            String ds = jobMessage.getDs();

            dateSet.add(ds);
            if (!cityList.contains(city)) {
                otherCityDemandMap.put(ds, otherCityDemandMap.getOrDefault(ds, 0) + cnt);
            }
        }

        // 日期 -> 城市 -> 简历需求映射表
        Map<String, Map<String, Integer>> dateCityDemandMap = new HashMap<>();

        for (JobMessage jobMessage : lists) {
            int cnt = jobMessage.getCnt();
            String city = jobMessage.getCity();
            String ds = jobMessage.getDs();

            // 只处理非其他城市的数据
            if (!cityList.contains(city)) {
                continue;
            }

            if (!dateCityDemandMap.containsKey(ds)) {
                dateCityDemandMap.put(ds, new HashMap<String, Integer>() {{
                    put(city, cnt);
                }});
            } else {
                dateCityDemandMap.get(ds).put(city, cnt);
            }
        }

        // 添加其他城市的简历需求
        for (String date : dateSet) {
            dateCityDemandMap.getOrDefault(date, null).put("其他", otherCityDemandMap.get(date));
        }

        System.out.println("treeSet " + dateSet);
        System.out.println("dateCityDemandMap " + dateCityDemandMap);


        Map<String, Object> dataMap = new LinkedHashMap<>();

        List<HashMap<String, Object>> seriesData = new LinkedList<>();
        for (String city : cityList) {
            List<Integer> dataList = new LinkedList<>();
            for (String date : dateSet) {
                dataList.add(dateCityDemandMap.get(date).get(city));
            }

            seriesData.add(new LinkedHashMap<String, Object>() {{
                put("name", city);
                put("data", dataList);
            }});
        }

        dataMap.put("categories", dateSet);
        dataMap.put("series", seriesData);

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", dataMap);
        return res;
    }

    public Map<String, Object> getResumeCountOfCity(String startDate, String endDate) {
        List<JobMessage> lists = dataMapper.selectResumeCountOfCity(startDate, endDate);

        int total = 0;
        Map<String, Integer> cityDemandMap = new HashMap<>();

        for (JobMessage jobMessage : lists) {
            String city = jobMessage.getCity();
            int cnt = jobMessage.getCnt();
            total += cnt;
            if (cityList.contains(city)) {
                cityDemandMap.put(city, cnt);
            } else {
                cityDemandMap.put("其他", cityDemandMap.getOrDefault("其他", 0) + cnt);
            }
        }

        System.out.println("total " + total);
        List<LinkedList<Object>> seriesData = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : cityDemandMap.entrySet()) {
            String city = entry.getKey();
            int cnt = entry.getValue();
            float ratio = (float) (cnt * 100) / total;

            seriesData.add(new LinkedList<Object>() {{
                add(city + " " + cnt);
                add(Float.parseFloat(String.format("%.2f", ratio)));
            }});
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", seriesData);
        return res;

    }

    public Map<String, Object> getRankData(String col) {
        List<JobMessage> lists = dataMapper.selectRankData(col);
        System.out.println(lists);

        List<Map<String, Object>> rankList = new LinkedList<>();
        for (JobMessage list : lists) {
            switch (col) {
                case "second_type":
                    rankList.add(new HashMap<String, Object>() {{
                        put("second_type", list.getSecondType());
                        put("cnt", list.getCnt());
                    }});
                    break;
                case "company_name":
                    rankList.add(new HashMap<String, Object>() {{
                        put("company_name", list.getCompanyName());
                        put("company_short_name", list.getCompnayShortName());
                        put("cnt", list.getCnt());
                    }});
                    break;
            }
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", rankList);
        return res;
    }

    public Map<String, Object> getOptions(String type) {
        List<JobMessage> lists = dataMapper.getOptions(type);

        List<LinkedHashMap<String, String>> listOfOptions = new LinkedList<>();
        listOfOptions.add(new LinkedHashMap<String, String>() {{
            put("label", "All");
            put("value", "");
        }});
        switch (type) {
            case "city":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getCity());
                        put("value", list.getCity());
                    }});
                }
                break;
            case "company_size":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getCompanySize());
                        put("value", list.getCompanySize());
                    }});
                }
                break;
            case "finance_stage":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getFinanceStage());
                        put("value", list.getFinanceStage());
                    }});
                }
                break;
            case "first_type":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getFirstType());
                        put("value", list.getFirstType());
                    }});
                }
                break;
            case "second_type":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getSecondType());
                        put("value", list.getSecondType());
                    }});
                }
                break;
            case "third_type":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getThirdType());
                        put("value", list.getThirdType());
                    }});
                }
                break;
            case "work_year":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getWorkYear());
                        put("value", list.getWorkYear());
                    }});
                }
                break;
            case "education":
                for (JobMessage list : lists) {
                    listOfOptions.add(new LinkedHashMap<String, String>() {{
                        put("label", list.getEducation());
                        put("value", list.getEducation());
                    }});
                }
                break;
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", listOfOptions);
        return res;
    }

    /**
     * @param type
     * @param city
     * @param startDate
     * @param endDate
     * @return
     */
    public Map<String, Object> getTrafficData(String type, String param, String startDate, String endDate) {
        String cons = DataProcessUtil.generateCons(param, startDate, endDate);

        List<JobMessage> lists = dataMapper.selectDistinctFieldOfParams(type, cons);

        List<HashMap<String, Object>> resMap = new LinkedList<>();
        for (JobMessage jobMessage : lists) {
            switch (type) {
                case "subway_line":
                    if (jobMessage.getSubwayLine() != "None") {
                        resMap.add(new HashMap<String, Object>() {{
                            put("name", jobMessage.getSubwayLine());
                            put("weight", jobMessage.getCnt());
                        }});
                    }
                    break;
                case "station_name":
                    if (jobMessage.getStationName() != "None") {
                        resMap.add(new HashMap<String, Object>() {{
                            put("name", jobMessage.getStationName());
                            put("weight", jobMessage.getCnt());
                        }});
                    }
                    break;
            }
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", resMap);
        return res;
    }


    public Map<String, Object> getBusinessZonesData(String type, String city, String startDate, String endDate) {
        List<JobMessage> lists = dataMapper.selectField(type, city, startDate, endDate);
        List<HashMap<String, Object>> resMap = new LinkedList<>();
        HashMap<String, Integer> frequencyMap = new HashMap<>();
        for (JobMessage jobMessage : lists) {
            if (jobMessage.getBusinessZones().length() > 0) {
                String[] labelArrs = jobMessage.getBusinessZones().split("\\|");
                for (String label : labelArrs) {
                    frequencyMap.put(label, frequencyMap.getOrDefault(label, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            resMap.add(new HashMap<String, Object>() {{
                put("name", entry.getKey());
                put("weight", entry.getValue());
            }});
        }


        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", resMap);
        return res;

    }

    public Map<String, Object> getAvgSalary(String param, String startDate, String endDate) {
        String cons = DataProcessUtil.generateCons(param, startDate, endDate);

        List<JobMessage> lists = dataMapper.selectSalary(cons, startDate, endDate);
        LinkedHashMap<Integer, String> salaryMap = RevenueProcessUtil.getRevenueMap();
        LinkedHashMap<String, Integer> resMap = new LinkedHashMap<>();

        // 填充薪酬范围哈希表
        for (Map.Entry<Integer, String> entry : salaryMap.entrySet()) {
            String salaryRange = entry.getValue();
            if (!resMap.containsKey(salaryRange)) {
                resMap.put(salaryRange, 0);
            }
        }

        int total = 0;
        for (JobMessage jobMessage : lists) {
            String salary = jobMessage.getSalary();

            int cnt = jobMessage.getCnt();
            salary = salary.substring(0, salary.length() - 1);

            String[] salaryArr = salary.split("k-");
            if (salaryArr.length < 2) {
                continue;
            }
            int salary1 = Integer.parseInt(salaryArr[0]) * 1000;
            int salary2 = Integer.parseInt(salaryArr[1]) * 1000;
            int avgSalary = (salary1 + salary2) / 2;

            String salaryRange = salaryMap.getOrDefault(avgSalary, "50k以上");
            resMap.put(salaryRange, resMap.get(salaryRange) + cnt);
            total += cnt;
        }


        System.out.println("total" + total);
        List<LinkedList<Object>> pieData = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : resMap.entrySet()) {
            int cnt = entry.getValue();
            String salaryRange = entry.getKey();
            float ratio = (float) (cnt * 100) / total;

            pieData.add(new LinkedList<Object>() {{
                add(salaryRange + " " + cnt);
                add(Float.parseFloat(String.format("%.2f", ratio)));
            }});
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", pieData);
        return res;

    }
}
