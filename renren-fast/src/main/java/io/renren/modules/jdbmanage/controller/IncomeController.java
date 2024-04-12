package io.renren.modules.jdbmanage.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.jdbmanage.entity.IncomeEntity;
import io.renren.modules.jdbmanage.service.IncomeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author 
 * @email 
 * @date 2024-03-26 15:07:39
 */
@RestController
@RequestMapping("jdbmanage/income")
public class IncomeController extends AbstractController {
    @Autowired
    private IncomeService incomeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("jdbmanage:income:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = incomeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("jdbmanage:income:info")
    public R info(@PathVariable("id") Integer id){
		IncomeEntity income = incomeService.getById(id);

        return R.ok().put("income", income);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("jdbmanage:income:save")
    public R save(@RequestBody IncomeEntity income){
		incomeService.save(income);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("jdbmanage:income:update")
    public R update(@RequestBody IncomeEntity income){
		incomeService.updateById(income);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("jdbmanage:income:delete")
    public R delete(@RequestBody Integer[] ids){
		incomeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/listArr")
    @RequiresPermissions("jdbmanage:income:list")
    public R listArr(){
        Long UserId = getUser().getUserId();
        return R.ok().put("list", incomeService.listAllData(UserId));
    }

    @RequestMapping("/getDistrict")
    @RequiresPermissions("jdbmanage:income:list")
    public R getDistrict(){
        return R.ok().put("list",incomeService.getDistrict(getUser().getUserId()));
    }

}
