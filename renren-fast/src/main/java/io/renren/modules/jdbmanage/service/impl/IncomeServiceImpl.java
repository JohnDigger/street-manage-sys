package io.renren.modules.jdbmanage.service.impl;

import io.renren.modules.jdbmanage.entity.IncomeStaticsEntity;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<IncomeEntity> page = this.page(
                new Query<IncomeEntity>().getPage(params),
                new QueryWrapper<IncomeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public IncomeStaticsEntity IncomeInfo(float start, float end) {
        Integer population = incomeDao.countPopulation(start,end);
        Double income = incomeDao.countIncome(start,end);
        DecimalFormat df = new DecimalFormat();
        IncomeStaticsEntity staticsEntity = new IncomeStaticsEntity();
        staticsEntity.setTotalPopulation(population);
        staticsEntity.setFamilyCountEntityList(incomeDao.listFamilys(start, end));
        staticsEntity.setTotalFamilys(incomeDao.countAllFamilys(start, end));
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
    public IncomeStaticsEntity IncomeInfoByRole(float start, float end, Integer street) {
        Integer population = incomeDao.countPopulationByRole(start, end, street);
        Double income = incomeDao.countIncomeByRole(start, end, street);
        DecimalFormat df = new DecimalFormat();
        IncomeStaticsEntity staticsEntity = new IncomeStaticsEntity();
        staticsEntity.setTotalPopulation(population);
        staticsEntity.setFamilyCountEntityList(incomeDao.listFamilysByRole(start, end, street));
        staticsEntity.setTotalFamilys(incomeDao.countAllFamilysByRole(start, end, street));
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
    public List<IncomeStaticsEntity> listAllData(Long userId) {
        List<IncomeStaticsEntity> incomeStaticsEntities = new ArrayList<>();
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);

        for (float step = 0; step < 3; step += 1.00) {
            IncomeStaticsEntity incomeStaticsEntity = roleIdList.size() != 0
                    ? IncomeInfoByRole(step, step + 1, Math.toIntExact(roleIdList.get(0)))
                    : IncomeInfo(step, step + 1);

            incomeStaticsEntity.setIncomeGroups(step == 0 ? "1万元及以下" : (int) step + "万元（不含）——" + (int) (step + 1) + "万元");

            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntitySubLow = IncomeInfoByRole(3, 3.33f, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntitySubLow.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中\n" +
                    "3.33万元以下");
            incomeStaticsEntities.add(incomeStaticsEntitySubLow);

            IncomeStaticsEntity incomeStaticsEntitySubHigh = IncomeInfoByRole(3.33f, 4f, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntitySubHigh.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中\n" +
                    "3.33万元以上");
            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        } else {
            IncomeStaticsEntity incomeStaticsEntitySubLow = IncomeInfo(3f, 3.33f);
            incomeStaticsEntitySubLow.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中\n" +
                    "3.33万元以下");

            incomeStaticsEntities.add(incomeStaticsEntitySubLow);

            IncomeStaticsEntity incomeStaticsEntitySubHigh = IncomeInfo(3.33f, 4f);
            incomeStaticsEntitySubHigh.setIncomeGroups("3万元（不含）-—4万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中\n" +
                    "3.33万元以上");
            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        }

        for (float step = 4f; step < 10.00; step += 1.00) {
            IncomeStaticsEntity incomeStaticsEntity = roleIdList.size() != 0
                    ? IncomeInfoByRole(step, step + 1, Math.toIntExact(roleIdList.get(0)))
                    : IncomeInfo(step, step + 1);

            incomeStaticsEntity.setIncomeGroups((int) step + "万元（不含）——" + (int) (step + 1) + "万元");

            incomeStaticsEntities.add(incomeStaticsEntity);
        }
        IncomeStaticsEntity incomeStaticsEntitySubLow;
        IncomeStaticsEntity incomeStaticsEntitySubHigh;
        if (roleIdList.size() != 0) {

            incomeStaticsEntitySubLow = IncomeInfoByRole(10, (float) 16.66, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntitySubLow.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中16.66万元以下");


            incomeStaticsEntities.add(incomeStaticsEntitySubLow);

            incomeStaticsEntitySubHigh = IncomeInfoByRole((float) 16.66, 20, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntitySubHigh.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中16.66万元以上");



            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        } else {
            incomeStaticsEntitySubLow = IncomeInfo(10, (float) 16.66);
            incomeStaticsEntitySubLow.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubLow.setSubIncomeGroups("其中16.66万元以下");


            incomeStaticsEntities.add(incomeStaticsEntitySubLow);
            incomeStaticsEntitySubHigh = IncomeInfo((float) 16.66, 20);
            incomeStaticsEntitySubHigh.setIncomeGroups("10万元（不含）—20万元");
            incomeStaticsEntitySubHigh.setSubIncomeGroups("其中16.66万元以上");

            incomeStaticsEntities.add(incomeStaticsEntitySubHigh);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfoByRole(20, 50, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntity.setIncomeGroups("20万元（不含）——50万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        } else {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfo(20, 50);
            incomeStaticsEntity.setIncomeGroups("20万元（不含）——50万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfoByRole(50, 60, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntity.setIncomeGroups("50万元（不含）——60万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        } else {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfo(50, 60);
            incomeStaticsEntity.setIncomeGroups("50万元（不含）——60万元");
            incomeStaticsEntities.add(incomeStaticsEntity);
        }

        if (roleIdList.size() != 0) {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfoByRole(60, Float.MAX_VALUE, Math.toIntExact(roleIdList.get(0)));
            incomeStaticsEntity.setIncomeGroups("60万元（不含）以上");
            incomeStaticsEntities.add(incomeStaticsEntity);
        } else {
            IncomeStaticsEntity incomeStaticsEntity = IncomeInfo(60, Float.MAX_VALUE);
            incomeStaticsEntity.setIncomeGroups("60万元（不含）以上");
            incomeStaticsEntities.add(incomeStaticsEntity);
        }


        return incomeStaticsEntities;
    }
}