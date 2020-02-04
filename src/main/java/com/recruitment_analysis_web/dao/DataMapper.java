package com.recruitment_analysis_web.dao;

import com.recruitment_analysis_web.entity.JobMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataMapper {

    List<JobMessage> selectCityDemand();

    List<JobMessage> selectCityDemandOfSecondType();

    List<JobMessage> selectCntOfFinanceStage(@Param("cons") String cons);

    List<JobMessage> selectCompanyLabelList(@Param("cons") String cons);

    List<JobMessage> selectSkillLabelList(@Param("cons") String cons);

    List<JobMessage> selectPositionAdvantageList(@Param("cons") String cons);

    List<JobMessage> selectCityDistrictDemand(String startDate, String endDate);

    List<JobMessage> selectResumeCountOfCityByDate(String startDate, String endDate);

    List<JobMessage> selectResumeCountOfCity(String startDate, String endDate);

    //  List<JobMessage> selectRankData(String col);
    List<JobMessage> selectRankData(@Param("col") String col);

    List<JobMessage> selectDistinctField(@Param("type") String type, @Param("city") String city, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<JobMessage> selectLocationData(@Param("city") String city, @Param("company_name") String company_name, @Param("date") String date);

    List<JobMessage> getOptions(@Param("type") String type);

    List<JobMessage> selectField(@Param("field") String filed, @Param("city") String city, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<JobMessage> selectSalary(@Param("cons") String cons, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<JobMessage> selectDistinctFieldOfParams(@Param("field") String field, @Param("cons") String cons);
}
