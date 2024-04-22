package io.renren.modules.jdbmanage.service.impl;

import io.renren.modules.jdbmanage.entity.IncomeStaticsEntity;
import io.renren.modules.sys.service.SysRoleService;
import io.renren.modules.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.jdbmanage.dao.IncomeDao;
import io.renren.modules.jdbmanage.entity.IncomeEntity;
import io.renren.modules.jdbmanage.service.IncomeService;


@Service("incomeService")
public class IncomeServiceImpl extends ServiceImpl<IncomeDao, IncomeEntity> implements IncomeService {
    @Autowired
    private IncomeDao incomeDao;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IncomeEntity> page = this.page(
                new Query<IncomeEntity>().getPage(params),
                new QueryWrapper<IncomeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IncomeStaticsEntity IncomeInfo(float start, float end,String startDate,String endDate) {
        Integer population = incomeDao.countPopulation(start,end,startDate,endDate);
        Double income = incomeDao.countIncome(start,end,startDate,endDate);
        DecimalFormat df = new DecimalFormat();
        IncomeStaticsEntity staticsEntity = new IncomeStaticsEntity();
        staticsEntity.setTotalPopulation(population);
        staticsEntity.setFamilyCountEntityList(incomeDao.listFamilys(start, end,startDate,endDate));
        staticsEntity.setTotalFamilys(incomeDao.countAllFamilys(start, end,startDate,endDate));
        staticsEntity.setTotalIncome(income);
        if (income == null|| population == null){
            staticsEntity.setAverageIncome(0.0);
        }else {
            df.setMaximumFractionDigits(2);
            String result = df.format(income/population.doubleValue());
            staticsEntity.setAverageIncome(Double.valueOf(result));
        }
        return staticsEntity;
    }

    @Override
    public IncomeStaticsEntity IncomeInfoByRole(float start, float end, Integer street,String startDate,String endDate) {
        Integer population = incomeDao.countPopulationByRole(start, end, street,startDate,endDate);
        Double income = incomeDao.countIncomeByRole(start, end, street,startDate,endDate);
        DecimalFormat df = new DecimalFormat();
        IncomeStaticsEntity staticsEntity = new IncomeStaticsEntity();
        staticsEntity.setTotalPopulation(population);
        staticsEntity.setFamilyCountEntityList(incomeDao.listFamilysByRole(start, end, street,startDate,endDate));
        staticsEntity.setTotalFamilys(incomeDao.countAllFamilysByRole(start, end, street,startDate,endDate));
        staticsEntity.setTotalIncome(income);
        if (income == null|| population == null){
            staticsEntity.setAverageIncome(0.0);
        }else {
            df.setMaximumFractionDigits(2);
            String result = df.format(income/population.doubleValue());
            staticsEntity.setAverageIncome(Double.valueOf(result));
        }
        return staticsEntity;
    }

    @Override
    public List<IncomeStaticsEntity> listAllData(Long userId, String startDate, String endDate) {
        List<IncomeStaticsEntity> incomeStaticsEntities = new ArrayList<>();
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);

        for (float step = 0; step < 3; step += 1.00) {
            IncomeStaticsEntity incomeStaticsEntity = roleIdList.size() != 0
                    ? IncomeInfoByRole(step, step + 1, Math.toIntExact(roleIdList.get(0)),startDate,endDate)
                    : IncomeInfo(step, step + 1,startDate,endDate);

            incomeStaticsEntity.setIncomeGroups(step == 0 ? "1万元及以下" : (int) step + "万元（不含）——" + (int) (step + 1) + "万元");

            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntitySubLow = IncomeInfoByRole(3, 3.33f, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntitySubLow.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中\n" +
                    "3.33万元以下");
            incomeStaticsEntities.add(incomeStaticsEntitySubLow);

            IncomeStaticsEntity incomeStaticsEntitySubHigh = IncomeInfoByRole(3.33f, 4f, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntitySubHigh.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中\n" +
                    "3.33万元以上");
            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        } else {
            IncomeStaticsEntity incomeStaticsEntitySubLow = IncomeInfo(3f, 3.33f,startDate,endDate);
            incomeStaticsEntitySubLow.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中\n" +
                    "3.33万元以下");

            incomeStaticsEntities.add(incomeStaticsEntitySubLow);

            IncomeStaticsEntity incomeStaticsEntitySubHigh = IncomeInfo(3.33f, 4f,startDate,endDate);
            incomeStaticsEntitySubHigh.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中\n" +
                    "3.33万元以上");
            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        }

        for (float step = 4f; step < 10.00; step += 1.00) {
            IncomeStaticsEntity incomeStaticsEntity = roleIdList.size() != 0
                    ? IncomeInfoByRole(step, step + 1, Math.toIntExact(roleIdList.get(0)),startDate,endDate)
                    : IncomeInfo(step, step + 1,startDate,endDate);

            incomeStaticsEntity.setIncomeGroups((int) step + "万元（不含）——" + (int) (step + 1) + "万元");

            incomeStaticsEntities.add(incomeStaticsEntity);
        }
        IncomeStaticsEntity incomeStaticsEntitySubLow;
        IncomeStaticsEntity incomeStaticsEntitySubHigh;
        if (roleIdList.size() != 0) {

            incomeStaticsEntitySubLow = IncomeInfoByRole(10, (float) 16.66, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntitySubLow.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中16.66万元以下");


            incomeStaticsEntities.add(incomeStaticsEntitySubLow);

            incomeStaticsEntitySubHigh = IncomeInfoByRole((float) 16.66, 20, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntitySubHigh.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中16.66万元以上");



            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        } else {
            incomeStaticsEntitySubLow = IncomeInfo(10, (float) 16.66,startDate,endDate);
            incomeStaticsEntitySubLow.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中16.66万元以下");


            incomeStaticsEntities.add(incomeStaticsEntitySubLow);
            incomeStaticsEntitySubHigh = IncomeInfo((float) 16.66, 20,startDate,endDate);
            incomeStaticsEntitySubHigh.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中16.66万元以上");

            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfoByRole(20, 50, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntity.setIncomeGroups("20万元（不含）——50万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        } else {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfo(20, 50,startDate,endDate);
            incomeStaticsEntity.setIncomeGroups("20万元（不含）——50万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfoByRole(50, 60, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntity.setIncomeGroups("50万元（不含）——60万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        } else {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfo(50, 60,startDate,endDate);
            incomeStaticsEntity.setIncomeGroups("50万元（不含）——60万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfoByRole(60, Float.MAX_VALUE, Math.toIntExact(roleIdList.get(0)),startDate,endDate);
            incomeStaticsEntity.setIncomeGroups("60万元（不含）以上");
            incomeStaticsEntities.add(incomeStaticsEntity);
        } else {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfo(60, Float.MAX_VALUE,startDate,endDate);
            incomeStaticsEntity.setIncomeGroups("60万元（不含）以上");
            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        double SUM_POPULATION = 0;
        for (IncomeStaticsEntity incomeStaticsEntity : incomeStaticsEntities) {
            if (incomeStaticsEntity.getTotalPopulation() != null){
                SUM_POPULATION += incomeStaticsEntity.getTotalPopulation();
            }

        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        for (int i = 0; i <incomeStaticsEntities.size() ; i++) {
            double INCOME_PERCENT;
            if (incomeStaticsEntities.get(i).getTotalPopulation() == null){
                incomeStaticsEntities.get(i).setIncomePercentage(0.0);
            }else{

                INCOME_PERCENT = (incomeStaticsEntities.get(i).getTotalPopulation()/SUM_POPULATION)*100;
                String result = df.format(INCOME_PERCENT);
                incomeStaticsEntities.get(i).setIncomePercentage(Double.valueOf(result));
            }

        }

        return incomeStaticsEntities;
    }

    @Override
    public String getDistrict(Long UserId) {
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(UserId);
        if (roleIdList.isEmpty()){
            return " ";
        }else{
            return sysRoleService.queryRoleName(roleIdList.get(0));
        }
    }
}