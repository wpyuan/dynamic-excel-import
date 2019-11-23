import com.github.excel.configs.ImportConfig;
import com.github.excel.utils.DynamicExcelUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * utils测试
 * @author: peiyuan.wang@hand-china.com
 * @create: 2019/11/5 9:59
 * @copyright: Copyright (c) 2019
 * @version: 0.0.1
 */
@Ignore
public class DynamicReadUtilsTest {
    /**
     * 多sheet页导入
     * @param
     * @throws
     * @return
     * @author peiyuan.wang@hand-china.com
     * @date
     */
    @Test
    public void moreSheetImport() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoData多Sheet.xlsx";
        List<ImportConfig> configs = new ArrayList<>();
        ImportConfig config = new ImportConfig();
        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(234L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectSheetConfigById(0L));
        config.getSheetConfig().setHeadRowNumber(4L);
        config.getSheetConfig().setAutoUpdate(false);
        configs.add(config);
        configs.add(config);
        configs.add(config);
        DynamicExcelUtils.read(fileName, configs);
    }

    @Test
    public void Import10w() {
        String fileName = "D:\\my-opensource-utils\\dynamic-excel-import\\src\\main\\resources\\template\\DemoDataExtraColumn.xlsx";
        List<ImportConfig> configs = new ArrayList<>();
        ImportConfig config = new ImportConfig();
        config.setTemplateColConfigs(TestDataSource.selectTemplateColConfigByTemplateConfigId(234L));
        config.setDataSource(TestDataSource.selectDataSourceById(1L));
        config.setSheetConfig(TestDataSource.selectSheetConfigById(0L));
        config.getSheetConfig().setHeadRowNumber(2L);
        config.getSheetConfig().setBatchCount(5000);
//        config.getSheetConfig().setAutoUpdate(false);
        configs.add(config);
        DynamicExcelUtils.read(fileName, configs);
    }
}
