/**
 * Created by Yuri Khechoyan on 2/6/2017.
 */

//Imports Java Libraries
import java.io.IOException;
import java.lang.*;
import java.util.*;
import java.io.FileOutputStream;

//Imports Apache POI Libraries for use of Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;


public class FileManage {

    //Creates String for storing
    //content on when file is being created
    String createdFile;

    //Creates Workbook - entire excel document
    Workbook workbook;
    //Creates Sheet
    Sheet sheet;

    //Initializes the row and column numbers
    int r = 0;
    int rowDel = 1;
    int colZero = 0;
    int colOne = 1;
    int colTwo = 2;
    int colThree = 3;
    int colFour = 4;
    int colFive = 5;

    //FileManage Constructor
    public FileManage(){

        //creates TimeStamp, converts - toString() & adds to file sheet name
        Date timeFile = new Date();
        createdFile = timeFile.toString();
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(createdFile));
    }

    //Method upon file creation - when program launches
   public void openFile(){

       //Creates a row for information to be stored in
        Row row = sheet.createRow(r);

       //Setting Default Values - Headers - creates cells for content to go inside of
       Cell cellFName = row.createCell(colZero);
       Cell cellLName = row.createCell(colOne);
       Cell cellNumber = row.createCell(colTwo);
       Cell cellReason = row.createCell(colThree);
       Cell cellCheckIn = row.createCell(colFour);
       Cell cellCheckOut = row.createCell(colFive);


       //Assigning each column number a designated name - what each column represents
       //This will be the very top row of the document - Headers
       cellFName.setCellValue("FIRST NAME");
       cellLName.setCellValue("LAST NAME");
       cellNumber.setCellValue("PHONE NUMBER");
       cellReason.setCellValue("REASON FOR VISIT");
       cellCheckIn.setCellValue("CHECK IN");
       cellCheckOut.setCellValue("CHECK OUT");

       //Method to output customer information to document
       addWorkbook();
   }

    //Method used to create the output for all customer entered information
    private void addWorkbook() {
        //Create a new output stream
        try{
          FileOutputStream output = new FileOutputStream("Todays_Visitors.xls");
          workbook.write(output);
          output.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Method used to add customer to record (excel document) when the 'Register' button is pressed
    public void addRecords(String firstname, String lastname, String mobileNumber, String Reason) throws IOException {

        //Converts the check in time date format to string format
        String checkInTime = new Date().toString();

        //Moves row - increment by 1 for next checkInTime - new customer
        r++;

        //Creates a new row for content to be placed inside of
        Row row = sheet.createRow(r);

        //Setting Cell locations for column identifiers - Headers
        Cell cellFName = row.createCell(colZero);
        Cell cellLName = row.createCell(colOne);
        Cell cellNumber = row.createCell(colTwo);
        Cell cellReason = row.createCell(colThree);
        Cell cellCheckIn = row.createCell(colFour);
        Cell cellCheckOut = row.createCell(colFive);

       cellFName.setCellValue(firstname);
       cellLName.setCellValue(lastname);
       cellNumber.setCellValue(mobileNumber);
       cellReason.setCellValue(Reason);
       cellCheckIn.setCellValue(checkInTime);
       cellCheckOut.setCellValue("");

       //Method to output customer information to document
       addWorkbook();
   }

   public void delRecords() throws IOException{

       //Creates new time instance
       //Converts the check out time date format to string format
       String checkOutTime = new Date().toString();

        //Initializes row variable & assigns the row value to current row
       //inside of column 4 - for checkOutTime to be printed to
       Row row = sheet.getRow(rowDel);

       //Setting Cell locations for column identifiers - Headers
       Cell cellCheckOut = row.createCell(colFive);

       cellCheckOut.setCellValue(checkOutTime);

       //Moves row - increment by 1 for next checkOutTime new customer
       rowDel++;

       //Method to output customer information to document
       addWorkbook();
   }

}
