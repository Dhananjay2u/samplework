/*
 * Main.java
 *
 * Created on January 11, 2007, 4:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rewards;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import rewards.ExcelWriter;

/**
 *
 * @author Pandiaraja
 */
public class Main {

	private static BufferedReader instream;
	private static String menuOption;
	public static final String MENU_STMT = "1";
        public static final String MENU_HOLD = "2";
        public static final String MENU_LO = "3";
        public static final String MENU_APPS = "4";
        public static final String MENU_SUM = "5";
        public static final String MENU_DDA = "6";
        public static final String MENU_MM = "7";
        public static final String MENU_DR = "8";
        public static final String MENU_IMXAP = "9";
        public static final String MENU_XD = "10";
        public static final String MENU_IMXHP = "11" ;
        public static final String MENU_XPO = "12";
        public static final String MENU_IWB = "13";
        public static final String MENU_XS = "14";
        public static final String MENU_REPORT = "15";
        public static final String MENU_QUIT = "16";
        public static final String MENU_LF = "xxx";


	public Main () {}

	public static void main(String args[]) throws IOException {

		System.out.println("\n\n                    Control-D Conversion Tool");
		mainMenu();
	}

	private static void mainMenu () throws IOException {
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("                 Please select an option from the following menu");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("1.  CS7         [CS738D, CS740D, CS743D, CS748D, CS750D]");
                System.out.println("2.  MANH,OLC    [CS091DMANH(HOLD), CS091DOLC(HOLD)]");
                System.out.println("3.  LF,LO,LM,RO [LF, LO, LM, RO]");
                System.out.println("4.  AP0         [APPS LETTER]");
                System.out.println("5.  SUM         [CS0 ST SUMMARY]");
                System.out.println("6.  DDA         [DDASTMTPART1, DDAAIRMAIL, DDAPART2END, DDASEAMAIL]");
                System.out.println("7.  MM,INT      [MM STTMTS, INT STTMTS]");
                System.out.println("8.  DR0         [DR0196(LOCAL), DR0196 (OVERSEA AIR), DR0196 (OVERSEA SER)]");
                System.out.println("9.  IMXAP       [IMXAP081, IMXAP101]");
                System.out.println("10. XD0         [XDO145(LOCAL), XDO145(OVERSEA AIR), XDO145(OVERSEA SER)]");
                System.out.println("11. IMXHP       [IMXHP542]");
                System.out.println("12. XP0         [XPO153, XPO180]");
                System.out.println("13. IWB,SWB     [IWB810L1, IWB810L2, SWB800L1]");
				System.out.println("14. XSO         [XSO]");
                System.out.println("15. Report Generation");
                System.out.println("16. Quit");

		System.out.println("-------------------------------------------------------------------------------");

		instream = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("ENTER YOUR OPTION : ");
		menuOption = instream.readLine();


		if(menuOption.equals("1")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("Selected option is '1' ");
                        System.out.println("File name should be 'CS7'");
			StmtParse stmtParser= new StmtParse();
			stmtParser.parse(menuOption);
			mainMenu();

                }

                if(menuOption.equals("2")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '2' ");
                        System.out.println("FILE NAME SHOULD BE 'CSO91DOLC(HOLD)' or 'CSO91D MANH(HOLD)'");
                        StmtParse holdParser= new StmtParse();
			holdParser.parse(menuOption);
			mainMenu();

                }
                if(menuOption.equals("3")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '3' ");
                        System.out.println("FILE NAME SHOULD BE 'LF' 'LO' or 'LM' or 'RO'");
                        StmtParse loParser= new StmtParse();
			loParser.parse(menuOption);
			mainMenu();

                }

                if(menuOption.equals("4")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '4' ");
                        System.out.println("FILE NAME SHOULD BE 'AP0'");
                        StmtParse appsParser= new StmtParse();
			appsParser.parse(menuOption);
			mainMenu();

                }

                if(menuOption.equals("5")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '5' ");
                        System.out.println("FILE NAME SHOULD BE 'SUM'");
                        StmtParse sumParser= new StmtParse();
			sumParser.parse(menuOption);
			mainMenu();

                }

                /*if(menuOption.equals("16")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS 6 ");
                        System.out.println("FILE NAME SHOULD BE 'LF' ");
                        StmtParse lfParser= new StmtParse();
			lfParser.parse(menuOption);
			mainMenu();

                }*/
                if(menuOption.equals("6")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '6' ");
                        System.out.println("FILE NAME SHOULD BE 'DDASTMTPART1' or DDAAIRMAIL' or 'DDAPART2END' or 'DDASEAMAIL' ");
                        StmtParse ddaParser= new StmtParse();
			ddaParser.parse(menuOption);
			mainMenu();

                }

                 if(menuOption.equals("7")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '7' ");
                        System.out.println("FILE NAME SHOULD BE 'MM' or 'INT'");
                        StmtParse mmParser= new StmtParse();
			mmParser.parse(menuOption);
			mainMenu();

                  }
                if(menuOption.equals("8")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '8' ");
                        System.out.println("FILE NAME SHOULD BE 'DR0'");
                        StmtParse drParser= new StmtParse();
			drParser.parse(menuOption);
			mainMenu();

                }

                 if(menuOption.equals("9")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '9' ");
                        System.out.println("FILE NAME SHOULD BE 'IMXAP'");
                        StmtParse imxapParser= new StmtParse();
			imxapParser.parse(menuOption);
			mainMenu();

                 }

                 if(menuOption.equals("10")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '10' ");
                        System.out.println("FILE NAME SHOULD BE 'XD0'");
                        StmtParse xdxpParser= new StmtParse();
			xdxpParser.parse(menuOption);
			mainMenu();

                 }

                 if(menuOption.equals("11")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '11' ");
                        System.out.println("FILE NAME SHOULD BE 'IMXHP'");
                        StmtParse imxhpParser= new StmtParse();
			imxhpParser.parse(menuOption);
			mainMenu();

                 }

                if(menuOption.equals("12")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '12' ");
                        System.out.println("FILE NAME SHOULD BE 'XP0'");
                        StmtParse xpoParser= new StmtParse();
			xpoParser.parse(menuOption);
			mainMenu();

                 }
                 if(menuOption.equals("13")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '13' ");
                        System.out.println("FILE NAME SHOULD BE 'IWB' or 'SWB'");
                        StmtParse iwbParser= new StmtParse();
			iwbParser.parse(menuOption);
			mainMenu();

                 }

                  if(menuOption.equals("14")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '14' ");
                        System.out.println("FILE NAME SHOULD BE 'XSO'");
                        StmtParse xsParser= new StmtParse();
			xsParser.parse(menuOption);
			mainMenu();

                  }

                 if(menuOption.equals("15")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("Selected option is '15' ");
                        System.out.println("Report Generation");
			ExcelWriter exWrite = new ExcelWriter();
			exWrite.writeExcelSheet();
			mainMenu();

                }

		 if(menuOption.equals("16")){
                        System.out.print("\n");
                        System.out.print("\n");
                        System.out.println("SELECTED OPTION IS '16' ");
			System.out.println("Quiting application...");
			System.exit(0);
		}
	}

}
