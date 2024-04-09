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
@TableName("family_income")
public class IncomeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 家庭主键ID
	 */
	@TableId
	private Integer id;
	/**
	 * 街道名称，如：1炳草岗街道,2瓜子坪街道
	 */
	private Integer street;
	/**
	 * 是否常驻人口：0流动人口，1常驻人口
	 */
	private Integer isPermanent;
	/**
	 * 家庭地址
	 */
	private String address;
	/**
	 * 家庭总人口数
	 */
	private Integer familyNum;
	/**
	 * 家庭年收入（万元）
	 */
	private Float familyIncome;
	/**
	 * 家庭人均收入（万元）
	 */
	private Float averageIncome;
	/**
	 * 人均收入区间：1万元以下（含）：1；1万元至3.3万元（不含）：2；3.3万元以上（含）：3
	 */
	private Integer incomeInterval;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 社区调查员
	 */
	private Long officer;
	/**
	 * 调查时间
	 */
	private Date createTime;
	/**
	 * 户主
	 */
	private String houseHolder;

}
