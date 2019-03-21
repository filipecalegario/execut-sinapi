package examplePOI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

//Teste

public class ExamplePOI {

    private static final String FILE_NAME = "CATALOGO_COMPOSICOES_ANALITICAS_EXCEL_05_2018.xls";

    public static void main(String[] args) {

        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = WorkbookFactory.create(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            for (int i = 0; i < 4; i++) {
            	// Skip the header
            	iterator.next();
            }
            while (iterator.hasNext()) {
				Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                String classe = currentRow.getCell(0).getStringCellValue();
                String codigo = currentRow.getCell(1).getStringCellValue();
                String descricao = currentRow.getCell(2).getStringCellValue();
                String unidade = currentRow.getCell(3).getStringCellValue();
                String coeficiente = currentRow.getCell(4).getStringCellValue();
                
                boolean isCompositionTitle = (!classe.equals("INSUMO") && !classe.equals("COMPOSICAO"));
                if(isCompositionTitle){
                	System.out.print("Título: ");
                } else {
                	System.out.print(" - ");
                }
                System.out.println(codigo + " " + descricao);
                
                
                
//                while (cellIterator.hasNext()) {
//                    Cell currentCell = cellIterator.next();
//                    String cellValue = currentCell.getStringCellValue();
//                    System.out.println(isCompositionTitle);
//                    
//                    
//                    //System.out.println(currentCell.getCellType());
//                    //System.out.print(currentCell.getStringCellValue() + " ");
//                    
//                    //getCellTypeEnum shown as deprecated for version 3.15
//                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
////                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
////                        System.out.print(currentCell.getStringCellValue() + "--");
////                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
////                        System.out.print(currentCell.getNumericCellValue() + "--");
////                    }
//
//                }
                //System.out.println();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

