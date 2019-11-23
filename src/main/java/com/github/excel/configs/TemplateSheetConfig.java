package com.github.excel.configs;

import com.github.excel.enums.UpdateWay;
import lombok.Data;

/**
 * 模板Sheet页配置
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/28 15:46
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class TemplateSheetConfig<T> {
    private Long templateSheetConfigId;

    private Long templateConfigId;

    /**
     * sheet页下标
     */
    private Long sheetNo;

    /**
     * sheet配置名称
     */
    private String sheetConfigName;

    /**
     * 数据库表名
     */
    private String tbName;

    /**
     * JAVA模型名
     */
    private String modelName;

    /**
     * 数据源Id
     */
    private Long dataSourceId;

    /**
     * 每batchCount条为一批存储数据库（根据mysql处理请求包大小来配置，无固定值，理应越接近峰值，效率越快）
     */
    private Integer batchCount;

    /**
     * 程序自动更新
     */
    private Boolean autoUpdate = Boolean.TRUE;

    /**
     * 更新方式，默认插入
     */
    private UpdateWay updateWay = UpdateWay.INSERT;

    /**
     * 指定unique数据库字段名
     */
    private String[] uniqueTbColNames;

    /**
     * 头底部所在行号（从1开始...）
     */
    private Long headRowNumber;

    /**
     * 是否启用
     */
    private String enableFlag;

}
