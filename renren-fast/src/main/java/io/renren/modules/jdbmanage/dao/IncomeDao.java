package io.renren.modules.jdbmanage.dao;

import io.renren.modules.jdbmanage.entity.IncomeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.jdbmanage.entity.FamilyCountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author 
 * @email 
 * @date 2024-03-26 15:07:39
 */
@Mapper
public interface IncomeDao extends BaseMapper<IncomeEntity> {
    //按照标准划分每类家庭
    List<FamilyCountEntity> listFamilys(@Param("start") float start,@Param("end") float end,@Param("startDate")String startDate,@Param("endDate") String endDate);
    //总人口统计,常住人口
    Integer countPopulation(@Param("start") float start,@Param("end") float end,@Param("startDate")String startDate,@Param("endDate") String endDate);
    //总户数
    Integer countAllFamilys(@Param("start") float start,@Param("end") float end,@Param("startDate")String startDate,@Param("endDate") String endDate);
    //总收入
    Double countIncome(@Param("start") float start,@Param("end") float end,@Param("startDate")String startDate,@Param("endDate") String endDate);
    //为社区提供的查询接口
    List<FamilyCountEntity> listFamilysByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street,@Param("startDate")String startDate,@Param("endDate") String endDate);

    Integer countPopulationByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street,@Param("startDate")String startDate,@Param("endDate") String endDate);

    Integer countAllFamilysByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street,@Param("startDate")String startDate,@Param("endDate") String endDate);

    Double countIncomeByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street,@Param("startDate")String startDate,@Param("endDate") String endDate);
}
