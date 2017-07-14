/*
 * ExcelReader.java
 *
 * Created on June 6, 2007, 11:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rewards;

/**
 *
 * @author 1241182
 */





import java.io.File;
import java.io.IOException;
import java.util.Vector;
import jxl.Workbook;
import jxl.Sheet;
import jxl.Cell;
import jxl.read.biff.BiffException;

public class ExcelReader {

    private String fileName ;

    private Workbook workbook ;
    private Sheet sheet ;
    private Cell cell ;
    private String cellData ;

    private int numberOfSheets = -1 ;

    private int sheetRows = -1 ;
    private int sheetCols = -1 ;

    private int currentRow = -1 ;

    public ExcelReader( String fileName ) {
        this.fileName = fileName ;
    }

    public int getSheets() throws InvalidXLSException {

        try {

            workbook = Workbook.getWorkbook( new File( fileName ) ) ;
            numberOfSheets = workbook.getNumberOfSheets();

            return numberOfSheets ;
        } catch ( IOException ex ) {
            throw new InvalidXLSException( ex.getMessage() );
        } catch ( BiffException ex ) {
            throw new InvalidXLSException( ex.getMessage() );
        }

    }

    public void readSheet( int sheetNumber ) throws InvalidXLSException {

        if( sheetNumber < 0 ) {
            throw new InvalidXLSException( "Invalid Sheet Number" );
        }

        if( sheetNumber > numberOfSheets ) {
            throw new InvalidXLSException( "Max Number of excel sheets exceeded" );
        }

        sheet = workbook.getSheet(sheetNumber);

        sheetRows = sheet.getRows();
        sheetCols = sheet.getColumns();

    }

    public boolean hasNext() {
        return ( currentRow < sheetRows );
    }

    public boolean next() {
        currentRow++;
        return  ( currentRow < sheetRows ) ;
    }

    public int getColumns() {
        return sheetCols ;
    }

    public String getStringValue( int currentCol ) throws InvalidXLSException {

        if( currentCol < 0 ) {
            throw new InvalidXLSException( "Invalid column Number" );
        }

        if( currentCol > sheetCols ) {
            throw new InvalidXLSException( "Max Number of columns exceeded" );
        }

        cell = sheet.getCell( currentCol, currentRow ) ;
        cellData = cell.getContents();

        return cellData ;
    }

    public void close() throws InvalidXLSException {

        if( workbook == null ) {
            throw new InvalidXLSException( "The workbook is null" );
        }

        workbook.close();
    }
}