package io.renren.modules.jdbmanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.jdbmanage.entity.MemberEntity;

import java.util.Map;

/**
 * 
 *
 * @author 
 * @email 
 * @date 2024-03-26 15:07:39
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

