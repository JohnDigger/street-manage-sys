package io.renren.modules.jdbmanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.jdbmanage.entity.IncomeEntity;
import io.renren.modules.jdbmanage.entity.IncomeStaticsEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author 
 * @email 
 * @date 2024-03-26 15:07:39
 */
public interface IncomeService extends IService<IncomeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    IncomeStaticsEntity IncomeInfo(float start, float end ,String startDate,String endDate);

    IncomeStaticsEntity IncomeInfoByRole(float start, float end ,Integer street,String startDate,String endDate);

    List<IncomeStaticsEntity> listAllData(Long userId,String startDate,String endDate);

    String getDistrict(Long UserId);
}

