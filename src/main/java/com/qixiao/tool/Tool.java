package com.qixiao.tool;

import cn.hutool.core.util.StrUtil;

/**
 * @BelongsProject: addressParse
 * @BelongsPackage: com.qixiao.tool
 * @Author: qixiao
 * @Email：651901286@qq.com
 * @CreateTime: 2022-11-14  11:10
 * @Description:
 * @Version: 1.0
 */
public class Tool {

    /**
     * 滑动窗口算法
     * 查找任意两个字符串最长的子串长度
     * 查找不到返回 0
     * @param text1
     * @param text2
     * @return
     */
    public static int longestCommonStringLength(String text1,String text2) {
        // 当前连续子串长度最大值
        int result = 0;

        if(StrUtil.isBlank(text1) || StrUtil.isBlank(text2)){
            return result;
        }

        if(text2.length() < text1.length()) {
            String tem = text1;
            text1 = text2;
            text2 = tem;
        }
        // text1长度
        int len = text1.length();

        /**
         * 核心算法
         */
        for (int i = 0;i < len;i++){
            int currentLen = 0;
            String currentString = "";
            for (int j = 0; j < len;j++){
                currentString += text1.substring(j,j+1);
                if(text2.contains(currentString)){
                    currentLen += 1;
                    result = Math.max(result, currentLen);
                } else {
                    currentString = "";
                    currentLen = 0;
                }
            }
        }

        return result;
    }
}
