package com.github.excel.configs;

import com.github.excel.enums.CellTypeEnum;
import lombok.Data;

import java.util.Map;

/**
 * 模板列详细配置
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/28 15:23
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class TemplateColConfig {

    /**
     * 模板列配置id
     */
    private Long templateColConfigId;

    /**
     * 模板Sheet页配置id
     */
    private Long templateSheetConfigId;

    /**
     * 对应列号
     */
    private Integer cellNo;

    /**
     * 数据库字段名
     */
    private String tbColumnName;

    /**
     * 数据库字段描述
     */
    private String tbColumnCommit;

    /**
     * 列类型
     */
    private CellTypeEnum cellType;

    /**
     * 列格式掩码
     */
    private String cellFormat;

    /**
     * 值集
     */
    private Map<String, String> keyValues;

    /**
     * 列最大长度
     */
    private int cellMaxLength;

    /**
     * 最大值
     */
    private String max;

    /**
     * 最小值
     */
    private String min;

    /**
     * 是否必填
     */
    private String RequiredFlag;
}
