package com.github.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.github.excel.bean.ImportErrMsg;
import com.github.excel.configs.ImportConfig;
import com.github.excel.configs.TemplateColConfig;
import com.github.excel.constants.DynamicExcelConstants;
import com.github.excel.exception.DynamicExcelConvertException;
import com.github.excel.exception.DynamicExcelSQLExecException;
import com.github.excel.exception.DynamicExcelValidateException;
import com.github.excel.exception.DynamiceExcelException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 动态读
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/27 14:21
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
public class DefaultDynamicReadListener<T> extends AnalysisEventListener<Map<Integer, String>> {
    private Logger LOGGER = LoggerFactory.getLogger(DefaultDynamicReadListener.class);
    /**
     * 错误信息
     */
    private List<ImportErrMsg> errorMsgs = new ArrayList<>();
    /**
     * 每隔3000条存储数据库
     */
    private Integer batchCount = 3000;
    /**
     * 当前行号
     */
    private int currentRowIndex = 0;
    /**
     * 当前批次号
     */
    private int currentBatchNumber = 1;
    /**
     * 当前批次需导入数据
     */
    List<Map<Integer, Object>> blockData = new ArrayList<Map<Integer, Object>>();
    /**
     * javaModel - List
     */
    List<T> javaModelData = new ArrayList<>();
    /**
     * 当前导入成功条数
     */
    private int currentSuccessCount = 0;

    /**
     * 全局导入配置
     */
    private ImportConfig configContext;

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        configContext = (ImportConfig) analysisContext.getCustom();
        invokeProcessor(data, analysisContext);
    }

    /**
     * 建议不重写
     * @param analysisContext
     * @throws 
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (blockData.size() != 0) {
            saveData((ImportConfig) analysisContext.getCustom());
        }
        doAfterRead(analysisContext);
    }

    /**
     * 读完后的逻辑
     * @param analysisContext
     * @throws 
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    public void doAfterRead(AnalysisContext analysisContext) {
        LOGGER.info("{}所有数据解析完成！", analysisContext.readWorkbookHolder().getFile().getName());
        LOGGER.info("错误信息：{}", JSON.toJSONString(errorMsgs));
        LOGGER.info("{}总共导入：{}条", analysisContext.readSheetHolder().getSheetName(), (currentRowIndex - analysisContext.readSheetHolder().getHeadRowNumber()));
        LOGGER.info("成功条数：{}", currentSuccessCount);
    }

    private void invokeProcessor(Map<Integer, String> data, AnalysisContext analysisContext) {
        invokeBegin(data, analysisContext);
        invokeIng(data, analysisContext);
        invokeEnd(data, errorMsgs, analysisContext);
    }

    public void invokeBegin(Map<Integer, String> data, AnalysisContext analysisContext) {
    }

    public void invokeEnd(Map<Integer, String> data, List<ImportErrMsg> errorMsgs, AnalysisContext analysisContext) {
    }

    private void invokeIng(Map<Integer, String> data, AnalysisContext analysisContext) {
        currentRowIndex = analysisContext.readRowHolder().getRowIndex() + 1;
        List<TemplateColConfig> templateColConfigs = configContext.getTemplateColConfigs();
        //data移除无关列（不在配置中出现的列视为无关列）
        Map<Integer, Object> validData = removeUncorrelatedFillBlankData(data, templateColConfigs);
        //取列配置进行数据校验并转换。 非空 长度 数据类型 值集
        try {
            validateConverterProcessor(validData, templateColConfigs);
            appendBlockDataAndJavaModelData(validData);
        } catch (DynamiceExcelException e) {
            LOGGER.error(e.getMessage());
        }

        if (blockData.size() >= getBatchCount(configContext.getSheetConfig().getBatchCount())) {
            saveData(configContext);
            currentBatchNumber++;
            clearBlockDataAndJavaModelData();
        }
    }
    /**
     * 移除不相关数据填充空白格值
     * @param data
     * @param templateColConfigs
     * @throws
     * @return java.util.Map<java.lang.Integer,java.lang.String>
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private Map<Integer, Object> removeUncorrelatedFillBlankData (Map<Integer, String> data, List<TemplateColConfig> templateColConfigs) {
        Map<Integer, Object> validData = new HashMap<>();
        data.forEach((index, cellValue) -> {
            if (templateColConfigs.stream().anyMatch(colConfig -> colConfig.getCellNo().equals(index))) {
                validData.put(index, cellValue);
            }
        });

        //补充空白格的值
        Map<Integer, Object> fillBlankData = new HashMap<>();
        templateColConfigs.stream().forEach(colConfig -> {
            if (!validData.containsKey(colConfig.getCellNo())) {
                fillBlankData.put(colConfig.getCellNo(), null);
            }
        });
        validData.putAll(fillBlankData);
        return validData;
    }

    private void validateConverterProcessor(Map<Integer, Object> data, List<TemplateColConfig> colConfigs) {
        validateConverterBegin(data, colConfigs);
        validateConverter(data, colConfigs);
        validateConverterEnd(data, colConfigs);
    }

    public void validateConverterBegin(Map<Integer, Object> data, List<TemplateColConfig> colConfigs){

    };

    public void validateConverterEnd(Map<Integer, Object> data, List<TemplateColConfig> colConfigs){

    };

    /**
     * 每一个字符串单元格校验及转换
     * <br><br>
     * 提前停止条件：
     * <li>未填代码，值集为空
     * <li>非法值/匹配值不唯一
     * @param data      来源行数据
     * @param colConfig 列配置
     * @param colIndex  列下标
     * @param cellValue 单元格值
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    public void eachStringValidateConverter(Map<Integer, Object> data, TemplateColConfig colConfig, Integer colIndex, String cellValue) {
        //TODO 值集数据源来源应有2种，配置快码自动读取，另一种直接修改set
        if (colConfig.getCellFormat() != null) {
            //如果值集含有多个相同value的key，那么转换失败
            List<String> convertKeys = new ArrayList<>();
            colConfig.getKeyValues().forEach((key, value) -> {
                if (cellValue.equals(value)) {
                    convertKeys.add(key);
                }
            });

            //非法值
            if (convertKeys.size() ==0) {
                errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.INVALID_VALUE));
                return;
            }

            //匹配结果不唯一
            if (convertKeys.size() > 1){
                errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelConvertException.NOT_UNIQUE));
                return;
            }

            data.put(colIndex, convertKeys.get(0));
        }
    }

    /**
     * 每一个数字单元格校验即转换
     * <br><br>
     * 提前停止条件：
     * <li>不是纯数字
     * @param data
     * @param colConfig
     * @param colIndex
     * @param cellValue
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    public void eachNumberValidateConverter(Map<Integer, Object> data, TemplateColConfig colConfig, Integer colIndex, String cellValue) {

        if (!NumberUtils.isNumber(cellValue)) {
            errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelConvertException.NOT_NUMBER));
            return;
        }

        //转换后的数字 TODO 数字的前端必须控制死格式
        BigDecimal convertAfterNumber;
        if (colConfig.getCellFormat() != null) {
            DecimalFormat decimalFormat = new DecimalFormat(colConfig.getCellFormat());
            convertAfterNumber = new BigDecimal(decimalFormat.format(new BigDecimal(cellValue)));
        } else {
            convertAfterNumber = new BigDecimal(cellValue);
        }
        data.put(colIndex, convertAfterNumber);

        //TODO 最大值/最小值是数字，建议在前端控死格式
        //最大值存在且是数字且数据大于最大值
        if (StringUtils.isNotEmpty(colConfig.getMax()) && NumberUtils.isNumber(colConfig.getMax()) && convertAfterNumber.compareTo(new BigDecimal(colConfig.getMax())) == 1) {
            errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.OVER_THE_MAXIMUM));
        }

        //最小值存在且是数字且数据小于最小值
        if (StringUtils.isNotEmpty(colConfig.getMin()) && NumberUtils.isNumber(colConfig.getMin()) && convertAfterNumber.compareTo(new BigDecimal(colConfig.getMin())) == -1){
            errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.LESS_THAN_THE_MINIMUM));
        }
    }

    /**
     * 每一个日期单元格校验即转换
     * 提前停止条件：
     * <li>不是纯日期
     * @param data
     * @param colConfig
     * @param colIndex
     * @param cellValue
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    public void eachDateValidateConverter(Map<Integer, Object> data, TemplateColConfig colConfig, Integer colIndex, String cellValue) {
        //转换后的日期
        Date converterAfterDate;
        //最大日期
        Date maxDate;
        //最小日期
        Date minDate;
        try {
            converterAfterDate = DateUtils.parseDate(cellValue, StringUtils.defaultIfEmpty(colConfig.getCellFormat(), "yyyy-MM-dd HH:mm:ss"));
            //不是自动导入的转换
            if (!configContext.getSheetConfig().getAutoUpdate()) {
                data.put(colIndex, converterAfterDate);
            }
        } catch (ParseException e) {
            errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelConvertException.NOT_DATE));
            return;
        }
        //TODO 日期范围最大最小建议在前端取值控死格式
        try {
            //日期最大限制存在且数据超过限制
            if (colConfig.getMax() != null) {
                maxDate = DateUtils.parseDate(colConfig.getMax(), StringUtils.defaultIfEmpty(colConfig.getCellFormat(), DynamicExcelConstants.Format.DEFAULT_DATE_TIME_FORMAT));
                if (converterAfterDate.after(maxDate)) {
                    errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.OVER_THE_MAXIMUM));
                }
            }
            //日期最小限制存在且数据小于限制
            if (colConfig.getMin() != null) {
                minDate = DateUtils.parseDate(colConfig.getMin(), StringUtils.defaultIfEmpty(colConfig.getCellFormat(), DynamicExcelConstants.Format.DEFAULT_DATE_TIME_FORMAT));
                if (converterAfterDate.before(minDate)) {
                    errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.LESS_THAN_THE_MINIMUM));
                }
            }
        } catch (ParseException e) {
            LOGGER.error("{}行{}列，转换异常。配置中的最大值/最小值日期转换异常", currentRowIndex, colIndex+1);
        }
    }

    /**
     * 整行数据做校验转换，校验转换失败不停止将继续下一个值的校验转换
     * @param data
     * @param colConfigs
     * @throws
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private void validateConverter(Map<Integer, Object> data, List<TemplateColConfig> colConfigs) {
        final int CURRENT_ROW_EXCEPTION_COUNT = errorMsgs.size();
        Map<Integer, Object> dataValidateConverter = new HashMap<>(data.size());
        dataValidateConverter.putAll(data);
        dataValidateConverter.forEach((colIndex, cellValue) -> {
            colConfigs.forEach(colConfig -> {
                if (colIndex.equals(colConfig.getCellNo())) {
                    //是否必填校验
                    if (DynamicExcelConstants.Flag.Y.equals(colConfig.getRequiredFlag()) && cellValue == null) {
                        errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.NULL));
                    }
                    if (cellValue != null) {
                        //是否超过最大长度
                        if (colConfig.getCellMaxLength()< ((String) cellValue).length()) {
                            errorMsgs.add(new ImportErrMsg(currentRowIndex, colIndex+1, DynamicExcelValidateException.MORE_THEN_LENGTH));
                        }
                        switch (colConfig.getCellType()) {
                            case STRING:
                                //字符串数据校验及转换
                                eachStringValidateConverter(data, colConfig, colIndex, (String) cellValue);
                                break;
                            case NUMBER:
                                //数字单元格校验即转换
                                eachNumberValidateConverter(data, colConfig, colIndex, (String) cellValue);
                                break;
                            case DATE:
                                //日期单元格校验即转换
                                eachDateValidateConverter(data, colConfig, colIndex, (String) cellValue);
                                break;
                            default:break;
                        }
                    }
                }
            });
        });

        if (CURRENT_ROW_EXCEPTION_COUNT != errorMsgs.size()) {
            //本行校验转换出现过异常，该行不做插入
            throw new DynamiceExcelException(currentRowIndex + DynamiceExcelException.CURRENT_ROW_EXCEPTION);
        }
    }

    /**
     * 存储数据库
     */
    private void saveData(ImportConfig config) {
        if (config.getDataSource() != null) {
            try {
                currentSuccessCount += batchUpdate(config);
            } catch (DynamicExcelSQLExecException e) {
                errorMsgs.add(new ImportErrMsg(e.getBathchNumber(), e.getExceptionSql(), e.getMessage(), ExceptionUtils.getStackTrace(e)));
            }
        } else {
            errorMsgs.add(new ImportErrMsg(currentBatchNumber, config.getSheetConfig().getSheetConfigName() + ", 未配置数据源，无法持久化！"));
        }
    }

    /**
     * 批量插入数据库
     * @param config
     * @throws 
     * @return int 成功条数
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    public int batchUpdate(ImportConfig config) {
        if (config.getSheetConfig().getAutoUpdate()) {
            switch (config.getDataSource().getDbType()) {
                case MYSQL:
                    return bathExceMysqlByUpdateWay(config);
                case ORACLE:
                    return bathExceORAByUpdateWay(config);
                default:break;
            }
        }

        return 0;
    }

    /**
     * 根据更新方式批量处理(mysql)
     * @param config
     * @throws
     * @return int
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private int bathExceMysqlByUpdateWay(ImportConfig config) {
        switch (config.getSheetConfig().getUpdateWay()) {
            case INSERT:
                return batchInsertMysql(config);
            case MERGE:
                return batchMergeMysql(config);
            default:break;
        }
        return 0;
    }

    /**
     * 根据更新方式批量处理(oracle)
     * @param config
     * @throws
     * @return int
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private int bathExceORAByUpdateWay(ImportConfig config) {
        switch (config.getSheetConfig().getUpdateWay()) {
            case INSERT:
                return batchInsertORA(config);
            case MERGE:
                return batchMergeORA(config);
            default:break;
        }
        return 0;
    }

    /**
     * 批量插入(mysql)
     * @param config
     * @throws 
     * @return int
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private int batchInsertMysql(ImportConfig config) {
        Connection conn = null;
        StringBuffer insertTable = new StringBuffer();
        insertTable.append("insert into ")
                .append(config.getSheetConfig().getTbName())
                .append(" (");
        config.getTemplateColConfigs().stream().sorted(Comparator.comparing(TemplateColConfig::getCellNo))
                .forEach(colConfig -> {
                    insertTable.append(" ")
                            .append(colConfig.getTbColumnName())
                            .append(",");
                });
        insertTable.delete(insertTable.length()-1, insertTable.length()).append(") VALUES");
        blockData.forEach(indexCellValueMap -> {
            insertTable.append(" ( ");
            /**
             *  一行数据
             *  index: 列号
             *  CellValue: 单元格值
             */
            indexCellValueMap.forEach((index, CellValue) -> {
                if (CellValue == null) {
                    insertTable.append(" null,");
                } else {
                    insertTable.append(" '")
                            .append(CellValue)
                            .append("',");
                }
            });
            insertTable.delete(insertTable.length()-1, insertTable.length()).append(" ),");
        });
        insertTable.delete(insertTable.length()-1, insertTable.length());
        try {
            Class.forName(config.getDataSource().getDriver()).newInstance();
            conn = DriverManager.getConnection(config.getDataSource().getUrl(), config.getDataSource().getUserName(), config.getDataSource().getPassword());
            PreparedStatement ps = conn.prepareStatement(insertTable.toString());
            ps.execute();
            ps.close();
            conn.close();
        } catch (InstantiationException e) {
            throw new DynamicExcelSQLExecException(getCurrentBatchNumber(), insertTable.toString(), "insert error." + ExceptionUtils.getRootCauseMessage(e), e);
        } catch (IllegalAccessException e) {
            throw new DynamicExcelSQLExecException(getCurrentBatchNumber(), insertTable.toString(), "insert error." + ExceptionUtils.getRootCauseMessage(e), e);
        } catch (ClassNotFoundException e) {
            throw new DynamicExcelSQLExecException(getCurrentBatchNumber(), insertTable.toString(), "insert error." + ExceptionUtils.getRootCauseMessage(e), e);
        } catch (SQLException e) {
            throw new DynamicExcelSQLExecException(getCurrentBatchNumber(), insertTable.toString(), "insert error." + ExceptionUtils.getRootCauseMessage(e), e);
        }
        return blockData.size();
    }

    /**
     * 批量合并更新(mysql)
     * @param config
     * @throws 
     * @return int
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    private int batchMergeMysql(ImportConfig config) {
        return 0;
    }


    /**
     * 批量插入(oracle)
     * @param config
     * @throws
     * @return int
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private int batchInsertORA(ImportConfig config) {
        return 0;
    }

    /**
     * 批量合并更新(oracle)
     * @param config
     * @throws
     * @return int
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    private int batchMergeORA(ImportConfig config) {
        return 0;
    }

    /**
     * 获取批次量，默认3000
     * @param batchCount
     * @throws 
     * @return java.lang.Integer
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    private Integer getBatchCount(Integer batchCount) {
        return ObjectUtils.defaultIfNull(batchCount, this.batchCount);
    }

    public int getCurrentBatchNumber() {
        return this.currentBatchNumber;
    }

    public List<ImportErrMsg> getErrorMsgs() {
        return this.errorMsgs;
    }

    public int getCurrentRowIndex() {
        return this.currentRowIndex;
    }

    public int getCurrentSuccessCount() {
        return this.currentSuccessCount;
    }
    
    /**
     * 行数据插入“块数据集合”、“转换为javaModel的块数据集合”
     * @param rowData
     * @throws 
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    private void appendBlockDataAndJavaModelData(Map<Integer, Object> rowData) {
        blockData.add(rowData);
        if (configContext.getSheetConfig().getModelName() != null) {
            //转换为javaModel的块数据集合
            Map<String, Object> javaModelMap = new HashMap<>();
            rowData.forEach((index, cellValue) -> {
                configContext.getTemplateColConfigs().forEach(colConfig -> {
                    if (index == colConfig.getCellNo()) {
                        //下划线转驼峰
                        String[] params = colConfig.getTbColumnName().toLowerCase().split(DynamicExcelConstants.Symbol.UNDER_LINE);
                        for (int i = 1; i < params.length; i++) {
                            params[i] = (String.valueOf(params[i].charAt(0))).toUpperCase() + (params.length > 1 ? params[i].substring(1) : "");
                        }
                        String paramName = StringUtils.join(params);
                        javaModelMap.put(paramName, cellValue);
                    }
                });
            });
            try {
                javaModelData.add(JSON.parseObject(JSON.toJSONString(javaModelMap), (Type) Class.forName(configContext.getSheetConfig().getModelName())));
            } catch (ClassNotFoundException e) {
                LOGGER.error("javaModel Class not found. convert fail", e);
            }
        }
    }
    
    /**
     * 清理gc
     * @param 
     * @throws 
     * @return void
     * @author peiyuan.wang@hand-china.com
     * @date  
     */
    private void clearBlockDataAndJavaModelData() {
        blockData.clear();
        javaModelData.clear();
    }

}
