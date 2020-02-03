package com.recruitment_analysis_web;

import com.recruitment_analysis_web.util.DataProcessUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@SpringBootTest
class RecruitmentAnalysisWebApplicationTests {

    //@Test
    void contextLoads() {
        System.out.println("asdzscxcasd");
    }

    @Test
    void testJson() {
        Map<String, Object> map = new HashMap<>();

//        map.put("data", new HashMap<String, Object>() {{
//            put("xAxis", new HashMap<String, Object>() {{
//                put("categories", new HashMap<String, Object>() {{
//                    put("0", "北京");
//                    put("1", "上海");
//                }});
//            }});
//        }});

        LinkedList<String> list = new LinkedList<String>(){{
            add("北京");
            add("上海");
            add("深圳");
        }};
        map.put("city", list);
        System.out.println(map);
        System.out.println(DataProcessUtil.transMapToJSON(map));
    }

}
