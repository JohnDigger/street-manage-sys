package io.renren.modules.jdbmanage.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author 
 * @email 
 * @date 2024-03-26 15:07:39
 */
@Data
@TableName("family_member")
public class MemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 家庭成员主键
	 */
	@TableId
	private Integer id;
	/**
	 * 家庭收入信息外键，关联family_income表
	 */
	private Integer incomeId;
	/**
	 * 家庭成员姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 工作单位
	 */
	private String unit;
	/**
	 * 年收入（万元）
	 */
	private Float income;

}
