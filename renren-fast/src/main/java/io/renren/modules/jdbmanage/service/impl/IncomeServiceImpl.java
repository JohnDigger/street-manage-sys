package io.renren.modules.jdbmanage.service.impl;

import io.renren.modules.jdbmanage.entity.IncomeStaticsEntity;
import io.renren.modules.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        IncomeStaticsEntity staticsEntity = new IncomeStaticsEntity();
        staticsEntity.setTotalPopulation(incomeDao.countPopulation(start,end));
        staticsEntity.setFamilyCountEntityList(incomeDao.listFamilys(start, end));
        System.out.println(incomeDao.listFamilys(start, end));
        staticsEntity.setTotalFamilys(incomeDao.countAllFamilys(start, end));
        staticsEntity.setTotalIncome(incomeDao.countIncome(start,end));
        return staticsEntity;
    }

    @Override
    public IncomeStaticsEntity IncomeInfoByRole(float start, float end, Integer street) {
        IncomeStaticsEntity staticsEntity = new IncomeStaticsEntity();
        staticsEntity.setTotalPopulation(incomeDao.countPopulationByRole(start, end, street));
        staticsEntity.setFamilyCountEntityList(incomeDao.listFamilysByRole(start, end, street));
        staticsEntity.setTotalFamilys(incomeDao.countAllFamilysByRole(start, end, street));
        staticsEntity.setTotalIncome(incomeDao.countIncomeByRole(start, end, street));
        return staticsEntity;
    }

    @Override
    public List<IncomeStaticsEntity> listAllData(Long userId) {
        List<IncomeStaticsEntity> incomeStaticsEntities = new ArrayList<>();
        for (float step = 0;step<10.00;step++){
            IncomeStaticsEntity incomeStaticsEntity = new IncomeStaticsEntity();

            List<Long> roleIdList =  sysUserRoleService.queryRoleIdList(userId);
            if (roleIdList.size() != 0){
                incomeStaticsEntity = IncomeInfoByRole(step,step+1, Math.toIntExact(roleIdList.get(0)));
            }else {
                incomeStaticsEntity = IncomeInfo(step,step+1);
            }
            if (step == 0){
                incomeStaticsEntity.setIncomeGroups("1万元及以下");
            }else{
                incomeStaticsEntity.setIncomeGroups((int) step+"万元（不含）——"+(int) (step+1)+"万元");
            }
            incomeStaticsEntities.add(incomeStaticsEntity);
        }



        return incomeStaticsEntities;
    }

}