package com.github.excel.exception;

import lombok.Data;

/**
 * 动态Excel执行异常基类
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/29 15:42
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Data
public class DynamiceExcelException extends RuntimeException {
    public final static String CURRENT_ROW_EXCEPTION = "行校验转换出现过异常，该行不做插入";
    private Integer rowIndex;
    private Integer colIndex;
    public DynamiceExcelException() {

    }

    public DynamiceExcelException(String message) {
        super(message);
    }

    public DynamiceExcelException(Throwable cause) {
        super(cause);
    }

    public DynamiceExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamiceExcelException(Integer rowIndex, Integer colIndex, String message, Throwable cause) {
        super(message, cause);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public DynamiceExcelException(Integer rowIndex, Integer colIndex, String message) {
        super(message);
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }
}
