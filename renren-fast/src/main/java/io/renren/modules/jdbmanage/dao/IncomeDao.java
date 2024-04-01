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
    List<FamilyCountEntity> listFamilys(float start, float end);
    //总人口统计,常住人口
    Integer countPopulation(float start, float end);
    //总户数
    Integer countAllFamilys(float start, float end);
    //总收入
    Double countIncome(float start,float end);
    //为社区提供的查询接口
    List<FamilyCountEntity> listFamilysByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street);
    Integer countPopulationByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street);
    Integer countAllFamilysByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street);
    Double countIncomeByRole(@Param("start") float start,@Param("end") float end,@Param("street") Integer street);
}
