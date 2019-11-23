package com.github.excel.exception;

import lombok.Data;

/**
 * 数据转换异常
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/29 12:20
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class DynamicExcelConvertException extends DynamiceExcelException {
    public final static String NOT_UNIQUE = "匹配值不唯一";
    public final static String NOT_NUMBER = "不是数字";
    public final static String NOT_DATE = "不是日期";
    public DynamicExcelConvertException(Integer rowIndex, Integer colIndex, String message) {
        super(message);
        this.setRowIndex(rowIndex);
        this.setColIndex(colIndex);
    }
}
