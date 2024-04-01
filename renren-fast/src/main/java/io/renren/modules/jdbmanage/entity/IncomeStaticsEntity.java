package io.renren.modules.jdbmanage.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 贾佳
 * @date 2024/3/27 8:55
 */
@Data
@Getter
@Setter
public class IncomeStaticsEntity {
    //收入群体
    private String incomeGroups;
    //总人数
    private Integer totalPopulation;
    //总户数
    private Integer totalFamilys;
    //总收入
    private Double totalIncome;
    //人均收入
    private Double averageIncome;
    //人均收入占比
    private Double incomePercentage;
    //家庭户数明细
    List<FamilyCountEntity> familyCountEntityList;
}
