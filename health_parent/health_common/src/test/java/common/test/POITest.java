package common.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class POITest {

  @Test
  void test() throws IOException {
    XSSFWorkbook workbook = new XSSFWorkbook("E:\\test.xlsx");
    List<String[]> list = new ArrayList<>();
    for (int sheetNum = 0, n = Objects.requireNonNull(workbook).getNumberOfSheets(); sheetNum < n; sheetNum++) {
      Sheet sheet = workbook.getSheetAt(sheetNum);
      if (sheet != null) {
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
          Row row = sheet.getRow(rowNum);
          if (row != null) {
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            String[] cells = new String[row.getPhysicalNumberOfCells()];
            for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
              Cell cell = row.getCell(cellNum);
              cells[cellNum] = getcellValue(cell);
            }
            list.add(cells);
          }
        }
      }
    }
    workbook.close();
    for (String[] rows : list) {
      System.out.println(Arrays.toString(rows));
    }
  }

  /**
   * 将单元格数据转为字符串类型
   * @param cell 当前单元格
   * @return 其字符串形式
   */
  private static String getcellValue(Cell cell) {
    if (cell == null) {
      return "";
    }
    // 如果当前单元格内容为日期类型，那么需要特殊处理
    String dataFormatString = cell.getCellStyle().getDataFormatString();
    if ("m/d/yy".equals(dataFormatString)) {
      return new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue());
    }
    // 如果当前单元格内容是一个数字，那么需要把它当成字符串来处理，以免出现 1 被读取为 1.0 的情况
    if (cell.getCellType() == CellType.NUMERIC) {
      cell.setCellType(CellType.STRING);
    }
    switch (cell.getCellType()) {
      case STRING: return String.valueOf(cell.getStringCellValue());
      case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
      case FORMULA: return String.valueOf(cell.getCellFormula());
      case BLANK: return "";
      case ERROR: return "非法字符";
      default: return "未知类型";
    }
  }

}
