package test;

import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.WritableFont;
import jxl.write.WritableCellFormat;
import jxl.write.Label;

public class ExcelWriter {

    private WritableCellFormat cellFormat;
    private WritableWorkbook workBook;
    protected  WritableSheet sheet;
    protected WritableFont headerFont = new WritableFont( WritableFont.ARIAL,10, WritableFont.BOLD, true );
	protected WritableFont dataFont = new WritableFont( WritableFont.ARIAL);
    private Label label;
    protected String fileName;

	protected void setHeaderFont(WritableFont headerFont) {
		this.headerFont = headerFont;
	}
	protected void setDataFont(WritableFont dataFont) {
		this.dataFont = dataFont;
	}

    public void init()throws Exception {

        try {
		    workBook = Workbook.createWorkbook( new File(fileName) );
        } catch ( IOException ioe ) {
        	System.out.println("error is originated here" + fileName);
            ioe.printStackTrace();
        }
    }

    public void close() {

        try {
            workBook.write();
            workBook.close();
			workBook = null;
			sheet = null;
        } catch( Exception ex ) {
            ex.printStackTrace();
        }
    }
	
	public void createSheet(String sheetName,int no) {
		sheet = null;
		try{
		sheet = workBook.createSheet(sheetName,no);
		}
		catch(Exception e)
		{
			System.out.println("ERROR IN SHEET " +e.getMessage());
			e.printStackTrace();
		}
	}

	public void writeHeader(int row, int col, String cellHeader) {

		try {
			cellFormat = new WritableCellFormat( headerFont );

            if( cellHeader == null )
                cellHeader = "" ;
			label = new Label( col , row ,  cellHeader, cellFormat );
			sheet.addCell( label ) ;
		} catch (Exception wex) {
			wex.printStackTrace();
		}
	}

    public void writeCellValue(int row, int col, String cellValue) {

        try {
	        cellFormat = new WritableCellFormat( dataFont );

            if( cellValue == null )
                cellValue = "" ;
            jxl.write.Number number3 = new jxl.write.Number(col, row,Double.parseDouble(cellValue));
  		    sheet.addCell(number3);

        } catch ( Exception wex ) {
            wex.printStackTrace();
        }

    }
    public void writeCellValue3(int row, int col, int cellValue) {

        try {
	        cellFormat = new WritableCellFormat( dataFont );

            if( cellValue == 0 )
                cellValue = 0 ;
            jxl.write.Number number3 = new jxl.write.Number(col, row,cellValue);
  		    sheet.addCell(number3);

        } catch ( Exception wex ) {
            wex.printStackTrace();
        }

    }
    
    public void writeCellValue4(int row, int col, double cellValue) {

        try {
	        cellFormat = new WritableCellFormat( dataFont );
            if( cellValue == 0 )
                cellValue = 0 ;
            jxl.write.Number number3 = new jxl.write.Number(col, row,cellValue);
  		    sheet.addCell(number3);

        } catch ( Exception wex ) {
            wex.printStackTrace();
        }

    }


    public void writeCellValue1(int row, int col, String cellValue) {

        try {

	        cellFormat = new WritableCellFormat( dataFont );

            if( cellValue == null )
                cellValue = "" ;
           	label = new Label( col, row, cellValue);
           	sheet.addCell( label );

        } catch ( Exception wex ) {
            wex.printStackTrace();
        }

    }
}