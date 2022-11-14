package com.qixiao.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.qixiao.entity.AreaDataEntity;
import com.qixiao.tool.Tool;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @BelongsProject: addressParse
 * @BelongsPackage: com.qixiao.service.impl
 * @Author: qixiao
 * @Email：651901286@qq.com
 * @CreateTime: 2022-11-14  10:55
 * @Description:
 * @Version: 1.0
 */
@Log4j2
@Service
public class AddressParseServiceImpl implements com.qixiao.service.AddressParseService {
    private final List<AreaDataEntity> areaDataEntityList;

    /**
     * 全匹配最大值
     */
    private final int MATCH_MAX_VALUE_ALL_PROVINCE = 8;
    private final int MATCH_MAX_VALUE_ALL_CITY = 7;
    private final int MATCH_MAX_VALUE_ALL_COUNTY = 6;

    /**
     * 匹配的最大值
     */
    private final int MATCH_MAX_VALUE = 4;

    /**
     * 匹配最小值
     */
    private final int MATCH_MIN_VALUE = 2;

    public AddressParseServiceImpl() {
        URL url = ResourceUtil.getResource("baidu_area_info.json");
        String jsonData = FileUtil.readString(url, Charset.defaultCharset());
        this.areaDataEntityList = JSONUtil.parseArray(jsonData).toList(AreaDataEntity.class);
        log.info("加载全国地区数据完成：{}",JSON.toJSONString(areaDataEntityList));
    }

    @Override
    public List<AreaDataEntity> loadData() {
        return areaDataEntityList;
    }

    @Override
    public AreaDataEntity parseByAddress(String address) {

        // 输入字符串不能为空
        if(StrUtil.isBlank(address)){
            return new AreaDataEntity();
        }

        // 匹配到的列表
        List<AreaDataEntity> matchList = new ArrayList<>();
        for (AreaDataEntity areaDataEntity : this.areaDataEntityList) {
            // 一级 省
            int provinceMatchValue = match(matchList, areaDataEntity, 0, address);
            if (CollUtil.isEmpty(areaDataEntity.getChildren())) {
                continue;
            }
            for (AreaDataEntity city : areaDataEntity.getChildren()) {
                // 二级 市
                int cityMatchValue = match(matchList, city, provinceMatchValue, address);
                if (CollUtil.isEmpty(city.getChildren())) {
                    continue;
                }
                for (AreaDataEntity county : city.getChildren()) {
                    match(matchList, county, provinceMatchValue + cityMatchValue, address);
                }
            }
        }

        List<AreaDataEntity> matchListSort = matchList.stream().sorted(Comparator.comparing(AreaDataEntity::getMatchValue, Comparator.reverseOrder())).collect(Collectors.toList());
        
        // 输出结果
        if(CollUtil.isEmpty(matchListSort)){
            // 匹配不到
            log.error("地址解析地理编码为空，入参：{}",address);
        } else {
            // 存在结果并且2个以上，看看有没有同时存在最优解
            if(matchListSort.size() >= 2) {
                if (matchListSort.get(0).getMatchValue().equals(matchListSort.get(1).getMatchValue())) {
                    log.warn("地址解析地理编码存在最少两个最优解，入参：{}，结果：{}",address,JSON.toJSONString(matchListSort));
                }
            }

            // 将最优结果返回
            return matchListSort.get(0);
        }

        return new AreaDataEntity();
    }

    public int match(List<AreaDataEntity> mathList,AreaDataEntity areaDataEntity,int matchValueParent,String text){
        int matchValue = 0;
        int matchValueMax = 0;

        // 数据校验
        if(ObjectUtil.isEmpty(areaDataEntity)) {
            return 0;
        }

        String matchContent;
        if("1".equals(areaDataEntity.getLevel())){
            matchContent = areaDataEntity.getProvince();
            matchValueMax = MATCH_MAX_VALUE_ALL_PROVINCE;
        } else if("2".equals(areaDataEntity.getLevel())) {
            matchContent = areaDataEntity.getCity();
            matchValueMax = MATCH_MAX_VALUE_ALL_CITY;
        } else if("3".equals(areaDataEntity.getLevel())) {
            matchContent = areaDataEntity.getCounty();
            matchValueMax = MATCH_MAX_VALUE_ALL_COUNTY;
        } else {
            return 0;
        }
        if (StrUtil.isNotBlank(matchContent) && text.contains(matchContent)){
            matchValue = matchValueMax;
        } else {
            matchValue = Tool.longestCommonStringLength(matchContent, text);
            // 匹配不能超过设置的最大值
            matchValue = Math.min(matchValue, MATCH_MAX_VALUE);
        }

        // 匹配值超过最小值才能算是匹配到，防止匹配到的数据量过多
        if(matchValue >= MATCH_MIN_VALUE){
            AreaDataEntity areaDataMatch  = areaDataEntity.getNewAreaDataEntity(areaDataEntity);
            areaDataMatch.setMatchValue(matchValue + matchValueParent);
            mathList.add(areaDataMatch);
        }
        return matchValue;
    }
}
