//package com.example.vocacyclicreview;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.read.biff.BiffException;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//
//public class Test extends AppCompatActivity {
//
//    ListView list_excel;
//    ArrayAdapter<String> arrayAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
////        Workbook workbook = null;
////        Sheet sheet = null;
////        try {
////            InputStream inputStream = getBaseContext().getResources().getAssets().open("test.xls");
////            workbook = Workbook.getWorkbook(inputStream);
////            sheet = workbook.getSheet(0);
////            int MaxColumn = 2, RowStart = 0, RowEnd = sheet.getColumn(MaxColumn - 1).length -1, ColumnStart = 0, ColumnEnd = sheet.getRow(2).length - 1;
////            for(int row = RowStart;row <= RowEnd;row++) {
////                String excelload = sheet.getCell(ColumnStart, row).getContents();
////                arrayAdapter.add(excelload);
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (BiffException e) {
////            e.printStackTrace();
////        } finally {
////            list_excel.setAdapter(arrayAdapter);
////            workbook.close();
////        }
//
//
//        try {
//            InputStream is = getBaseContext().getResources().getAssets().open("test.xlsx");
//            Workbook wb = Workbook.getWorkbook(is);
//
//            if(wb != null) {
//                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
//                if(sheet != null) {
//                    int colTotal = sheet.getColumns();    // 전체 열
//                    int rowIndexStart = 1;                  // row 인덱스 시작 (첫째줄 카테고리 제목 제외)
//                    int rowTotal = sheet.getColumn(colTotal-1).length; // 전체 행row
//
//                    // 한 개의 row 가 하나의 단어, 한 개의 col이 하나의 영역
//                    ArrayList<String> vocaEachArea = new ArrayList<>();
//                    for(int row=rowIndexStart;row<rowTotal;row++) {
//
//                        for(int col=0;col<colTotal;col++) {
//                            String contents = sheet.getCell(col, row).getContents();
//                            vocaEachArea.add(contents);
//                        }
//                        Log.i("test", vocaEachArea.toString());
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            FileInputStream file = new FileInputStream("D:/tmp/upload/right_excel/test.xlsx");
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//
//            int rowindex = 0;
//            int columnindex = 0;
//
//            //시트 수 (첫번째에만 존재하므로 0을 준다)
//            //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            //행의 수
//            int rows = sheet.getPhysicalNumberOfRows();
//            for (rowindex = 1; rowindex < rows; rowindex++) {
//                XSSFRow row = sheet.getRow(rowindex);
//                if (row != null) {
//                    //셀의 수
//                    int cells = row.getPhysicalNumberOfCells();
//                    for (columnindex = 0; columnindex <= cells; columnindex++) {
//                        //셀값을 읽는다
//                        XSSFCell cell = row.getCell(columnindex);
//                        String value = "";
//                        //셀이 빈값일경우를 위한 널체크
//                        if (cell == null) {
//                            continue;
//                        } else {
//                            //타입별로 내용 읽기
//                            switch (cell.getCellType()) {
//                                case FORMULA:
//                                    value = cell.getCellFormula();
//                                    break;
//                                case NUMERIC:
//                                    value = cell.getNumericCellValue() + "";
//                                    break;
//                                case STRING:
//                                    value = cell.getStringCellValue() + "";
//                                    break;
//                                case BLANK:
//                                    value = cell.getBooleanCellValue() + "";
//                                    break;
//                                case ERROR:
//                                    value = cell.getErrorCellValue() + "";
//                                    break;
//                            }
//                        }
//                    }
//                    // 내용 없는 단어 객체에 내용 채워넣기
//                    voca.set(0, new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), vocaEachArea.get(0), null, getTime));
//                    voca.set(1, new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), vocaEachArea.get(1), null, Integer.toString(0)));
//                    for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
//                        voca.set(i+2, new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaEachArea.get(i+2), null, null));
//                    }
//                    vocaList.add(voca);
//                }
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//
//            if(wb != null) {
//                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
//                if(sheet != null) {
//                    int colTotal = sheet.getColumns();    // 전체 열
//                    int rowIndexStart = 1;                  // row 인덱스 시작 (첫째줄 카테고리 제목 제외)
//                    int rowTotal = sheet.getColumn(colTotal-1).length; // 전체 행row
//
//                    // 한 개의 row 가 하나의 단어, 한 개의 col이 하나의 영역
//                    for(int row=rowIndexStart;row<rowTotal;row++) {
//                        ArrayList<String> vocaEachArea = new ArrayList<>();
//                        for(int col=0;col<colTotal;col++) {
//                            String contents = sheet.getCell(col, row).getContents();
//                            vocaEachArea.add(contents);
//                        }
//                        // 내용 없는 단어 객체에 내용 채워넣기
//                        voca.set(0, new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), vocaEachArea.get(0), null, getTime));
//                        voca.set(1, new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), vocaEachArea.get(1), null, Integer.toString(0)));
//                        for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
//                            voca.set(i+2, new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaEachArea.get(i+2), null, null));
//                        }
//                        vocaList.add(voca);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//        try {
//            FileInputStream file = new FileInputStream("D:/tmp/upload/right_excel/test.xlsx");
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//
//            int rowindex=0;
//            int columnindex=0;
//            //시트 수 (첫번째에만 존재하므로 0을 준다)
//            //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
//            XSSFSheet sheet=workbook.getSheetAt(0);
//            //행의 수
//            int rows=sheet.getPhysicalNumberOfRows();
//            for(rowindex=0;rowindex<rows;rowindex++){
//                //행을읽는다
//                XSSFRow row=sheet.getRow(rowindex);
//                if(row !=null){
//                    //셀의 수
//                    int cells=row.getPhysicalNumberOfCells();
//                    for(columnindex=0; columnindex<=cells; columnindex++){
//                        //셀값을 읽는다
//                        XSSFCell cell=row.getCell(columnindex);
//                        String value="";
//                        //셀이 빈값일경우를 위한 널체크
//                        if(cell==null){
//                            continue;
//                        }else{
//                            //타입별로 내용 읽기
//                            switch (cell.getCellType()){
//                                case FORMULA:
//                                    value=cell.getCellFormula();
//                                    break;
//                                case NUMERIC:
//                                    value=cell.getNumericCellValue()+"";
//                                    break;
//                                case STRING:
//                                    value=cell.getStringCellValue()+"";
//                                    break;
//                                case BLANK:
//                                    value=cell.getBooleanCellValue()+"";
//                                    break;
//                                case ERROR:
//                                    value=cell.getErrorCellValue()+"";
//                                    break;
//                            }
//                        }
//                        System.out.println(rowindex+"번 행 : "+columnindex+"번 열 값은: "+value);
//                    }
//
//                }
//            }
//
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
