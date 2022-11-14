package com.qixiao.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @BelongsProject: addressParse
 * @BelongsPackage: com.qixiao.entity
 * @Author: qixiao
 * @Email：651901286@qq.com
 * @CreateTime: 2022-11-14  10:31
 * @Description:
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class AreaDataEntity {
    /**
     * id
     * 200
     */
    @JSONField(ordinal = 1)
    private String id;

    /**
     * 省
     * 北京
     */
    @JSONField(ordinal = 2)
    private String province;

    /**
     * 市
     * 北京市
     */
    @JSONField(ordinal = 3)
    private String city;

    /**
     * 县/区
     * 东城区
     */
    @JSONField(ordinal = 4)
    private String county;

    /**
     * 父级地理编码
     */
    @JSONField(ordinal = 5)
    private String pCode;

    /**
     * 城市地理编码
     */
    @JSONField(ordinal = 6)
    private String code;

    /**
     * 所在的级别
     * 1-省，2-市，3-县/区
     */
    @JSONField(ordinal = 7)
    private String level;

    /**
     * 2位地理编码
     */
    @JSONField(ordinal = 8)
    private String shortCode;

    /**
     * 孩子节点
     */
    @JSONField(ordinal = 9)
    private List<AreaDataEntity> children;

    /**
     * 匹配值
     */
    private Integer matchValue;

    /**
     * 获取一个新的对象，不要孩子节点
     * @param value
     * @return
     */
    public AreaDataEntity getNewAreaDataEntity(AreaDataEntity value){
        AreaDataEntity areaDataEntity = new AreaDataEntity();
        areaDataEntity.setId(value.getId());
        areaDataEntity.setProvince(value.getProvince());
        areaDataEntity.setCity(value.getCity());
        areaDataEntity.setCounty(value.getCounty());
        areaDataEntity.setPCode(value.getPCode());
        areaDataEntity.setCode(value.getCode());
        areaDataEntity.setLevel(value.getLevel());
        areaDataEntity.setShortCode(value.getCode());
        areaDataEntity.setMatchValue(value.getMatchValue());
        return areaDataEntity;
    }
}
