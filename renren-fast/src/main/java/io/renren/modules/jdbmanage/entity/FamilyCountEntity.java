package io.renren.modules.jdbmanage.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 贾佳
 * @date 2024/3/26 17:27
 */
@Data
@Getter
@Setter
public class FamilyCountEntity {

    //家庭规模
    private String familySize;
    //总户数
    private Integer FamilysCount;



}
