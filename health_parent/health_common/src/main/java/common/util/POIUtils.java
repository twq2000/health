package common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class POIUtils {
  private final static String XLS = "xls";
  private final static String XLSX = "xlsx";
  private final static String DATE_FORMAT = "yyyy/MM/dd";

  public static List<String[]> readExcel(MultipartFile file) throws IOException {
    checkFile(file);
    // 至此，已确认 file != null && file==excel
    Workbook workbook = getWorkBook(file);
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
    return list;
  }

  /**
   * 判断用户上传的文件是否为excel文件
   * @param file 待上传文件
   * @throws IOException
   */
  private static void checkFile(MultipartFile file) throws IOException {
    if (file == null) {
      throw new FileNotFoundException("文件不存在！");
    }
    String fileName = file.getOriginalFilename();
    // 判断该文件是否为excel文件
    if (!Objects.requireNonNull(fileName).endsWith(XLS) && !fileName.endsWith(XLSX)) {
      throw new IOException(file + "不是excel文件！");
    }
  }

  /**
   * 根据excel版本，创建对应的WorkBook工作簿对象
   * @param file excel文件
   * @return
   */
  private static Workbook getWorkBook(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    try {
      InputStream inputStream = file.getInputStream();
      if (Objects.requireNonNull(fileName).endsWith(XLS)) {
        return new HSSFWorkbook(inputStream);
      }
      else {
        return new XSSFWorkbook(inputStream);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
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
      return new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
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
