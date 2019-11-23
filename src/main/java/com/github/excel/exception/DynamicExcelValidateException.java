package com.github.excel.exception;

import lombok.Data;

/**
 * 数据验证异常
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/29 12:20
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class DynamicExcelValidateException extends DynamiceExcelException {
    public final static String NULL = "空值";
    public final static String MORE_THEN_LENGTH = "超过最大长度";
    public final static String INVALID_VALUE = "非法值";
    public final static String OVER_THE_MAXIMUM = "超过最大值";
    public final static String LESS_THAN_THE_MINIMUM = "小于最小值";

    public DynamicExcelValidateException(Integer rowIndex, Integer colIndex, String message) {
        super(message);
        this.setRowIndex(rowIndex);
        this.setColIndex(colIndex);
    }
}
