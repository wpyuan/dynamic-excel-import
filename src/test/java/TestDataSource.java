import com.github.excel.configs.DataSource;
import com.github.excel.configs.TemplateColConfig;
import com.github.excel.configs.TemplateSheetConfig;
import com.github.excel.enums.CellTypeEnum;
import com.github.excel.enums.DBTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/10/30 15:10
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 * @modified by:
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 */
public class TestDataSource {

    public static DataSource selectDataSourceById(Long dataSourceId) {
        DataSource dataSource = new DataSource();
        //正常
        if (dataSourceId == 1L) {
            dataSource.setDataSourceName("测试导入用");
            dataSource.setUrl("jdbc:mysql://localhost:3306/testhap_hap_dev?allowMultiQueries=true&useSSL=false&serverTimezone=UTC");
        }

        //端口异常
        if (dataSourceId == -1L) {
            dataSource.setDataSourceName("测试导入用-端口异常");
            dataSource.setUrl("jdbc:mysql://localhost:3307/testhap_hap_dev?allowMultiQueries=true&useSSL=false&serverTimezone=UTC");
        }

        dataSource.setDbType(DBTypeEnum.MYSQL);
        dataSource.setDriver("com.mysql.jdbc.Driver");
        dataSource.setUserName("testHAP");
        dataSource.setPassword("testHAP");
        return dataSource;
    }

    public static TemplateSheetConfig selectSheetConfigById(Long templateSheetConfigId) {
        TemplateSheetConfig sheetConfig = new TemplateSheetConfig();
        if (templateSheetConfigId == 0L) {
            sheetConfig.setSheetNo(0L);
            sheetConfig.setTbName("demo_import_data");
            sheetConfig.setSheetConfigName("第一个sheet页");
            sheetConfig.setBatchCount(100000);
        }

        return sheetConfig;
    }

    public static TemplateSheetConfig selectCloseAutoUpdate() {
        TemplateSheetConfig sheetConfig = new TemplateSheetConfig();
        sheetConfig.setSheetNo(0L);
        sheetConfig.setTbName("demo_import_data");
        sheetConfig.setSheetConfigName("第一个sheet页-关闭自动更新");
        sheetConfig.setBatchCount(100000);
        sheetConfig.setAutoUpdate(false);
        return sheetConfig;
    }

    public static TemplateSheetConfig selectSaveJaveModelList() {
        TemplateSheetConfig sheetConfig = new TemplateSheetConfig();
        sheetConfig.setSheetNo(0L);
        sheetConfig.setTbName("demo_import_data");
        sheetConfig.setSheetConfigName("第一个sheet页-关闭自动更新");
        sheetConfig.setBatchCount(100000);
        sheetConfig.setAutoUpdate(false);
        sheetConfig.setModelName("com.github.excel.DefaultJavaModel");
        return sheetConfig;
    }

    /**
     * 根据头查询配置行
     * @param templateId
     * @throws
     * @return java.util.List<com.github.excel.configs.TemplateColConfig>
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    public static List<TemplateColConfig> selectTemplateColConfigByTemplateConfigId (Long templateId) {
        List<TemplateColConfig> templateColConfigs = new ArrayList<>();
        TemplateColConfig templateColConfig = new TemplateColConfig();

        if (templateId == 123L) {
            //第一列
            templateColConfig.setCellNo(0);
            templateColConfig.setTbColumnName("COL_STRING");
            templateColConfig.setCellType(CellTypeEnum.STRING);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("Y");
            templateColConfigs.add(templateColConfig);

            //第二列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(1);
            templateColConfig.setTbColumnName("COL_DATE");
            templateColConfig.setCellType(CellTypeEnum.DATE);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("N");
            templateColConfigs.add(templateColConfig);

            //第三列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(2);
            templateColConfig.setTbColumnName("COL_NUMBER");
            templateColConfig.setCellType(CellTypeEnum.NUMBER);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("Y");
            templateColConfigs.add(templateColConfig);
        } else if (templateId == 234L){
            //第二列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(1);
            templateColConfig.setTbColumnName("COL_DATE");
            templateColConfig.setCellType(CellTypeEnum.DATE);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("N");
            templateColConfigs.add(templateColConfig);

            //第三列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(2);
            templateColConfig.setTbColumnName("COL_NUMBER");
            templateColConfig.setCellType(CellTypeEnum.NUMBER);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("N");
            templateColConfigs.add(templateColConfig);

            //第四列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(3);
            templateColConfig.setTbColumnName("COL_DOUBLE");
            templateColConfig.setCellType(CellTypeEnum.NUMBER);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("N");
            templateColConfigs.add(templateColConfig);
        } else if (templateId == 2340L) {
            //第2列
            templateColConfig.setCellNo(1);
            templateColConfig.setTbColumnName("COL_STRING");
            templateColConfig.setCellType(CellTypeEnum.STRING);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("Y");
            templateColConfigs.add(templateColConfig);

            //第3列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(2);
            templateColConfig.setTbColumnName("COL_DATE");
            templateColConfig.setCellType(CellTypeEnum.DATE);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("N");
            templateColConfigs.add(templateColConfig);

            //第4列
            templateColConfig = new TemplateColConfig();
            templateColConfig.setCellNo(3);
            templateColConfig.setTbColumnName("COL_NUMBER");
            templateColConfig.setCellType(CellTypeEnum.NUMBER);
            templateColConfig.setCellMaxLength(100);
            templateColConfig.setRequiredFlag("Y");
            templateColConfigs.add(templateColConfig);
        }
        return templateColConfigs;
    }

}
