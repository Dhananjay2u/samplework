package com.sc.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.sc.util.FileFinder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sc.xml.xls.FieldMapping;
import com.sc.xml.xls.GenExcelData;
 
class XslHandler extends DefaultHandler
{
	
	private boolean bfname = false;
	private boolean blname = false;
	private boolean bnname = false;
	private boolean bsalary = false;
	private String strQname="";
	private String strQname1="";
	private String tempVal;
	private String strVal;
	private ArrayList<FieldMapping> fieldList=new ArrayList<FieldMapping>();			
	private FieldMapping fmap=new FieldMapping();
	
	
	public ArrayList<FieldMapping> getFieldList() {
		return fieldList;
	}

	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {
		
		tempVal="";
		strQname=qName.substring(qName.indexOf(":")+1,qName.length());
		
		StringBuffer sb = new StringBuffer();		
		
		
		if ((strQname.equalsIgnoreCase("stylesheet") || 
				strQname.equalsIgnoreCase("template") ||			
		        strQname.equalsIgnoreCase("Envelope") ||
		        strQname.equalsIgnoreCase("Header") ||		       
		        strQname.equalsIgnoreCase("ServiceHeader") ||
		        strQname.equalsIgnoreCase("userNameToken") ||		        
		        strQname.equalsIgnoreCase("userName") ||
		        strQname.equalsIgnoreCase("password") ||
		strQname.equalsIgnoreCase("consumerID") ||
		strQname.equalsIgnoreCase("if") ||
		strQname.equalsIgnoreCase("when") ||
		strQname.equalsIgnoreCase("variable") ||
		strQname.equalsIgnoreCase("Body") ||
		strQname.equalsIgnoreCase("choose") ||
		strQname.equalsIgnoreCase("password")))		{
			
			if(strQname.equals("choose"))
			{
				
			}
			
		}else{
			if (!strQname.equalsIgnoreCase("value-of") && (!strQname.equalsIgnoreCase("otherwise"))){
				//System.out.println("ISIS FIELD:" +strQname);
				
				fmap.setIsisField(strQname);
				//strVal=strQname+",";
				//sb.append(strQname+",");
			}
			
			
			if (attributes.getValue("select" )!="" && attributes.getValue("select" )!=null){
				
				String eopsField=attributes.getValue("select").replaceAll(":", ".").substring(attributes.getValue("select").replaceAll(":", ".").lastIndexOf("KeyValue/")+9);
				//String strfld=new String(eopsField);
				
				String atr[]=eopsField.split(",");
				//System.out.println(atr[0]);
				eopsField=atr[0];
				
				//System.out.println("EOPS MAPPING FIELD:" +eopsField );
				fmap.setEopsField(eopsField);
				//strVal=strVal+attributes.getValue("select").replaceAll(":", ".");
				//sb.append(attributes.getValue("select").replaceAll(":", ".")+"\n");

			}
		
			//System.out.println(strVal);
			
			//if (attributes.getValue("select" )!=null)
				//localMap.put(strQname,attributes.getValue("select" ));
			
		} 
		
 
		//System.out.println("Start Element :" +strQname.substring(strQname.indexOf(":")+1,strQname.length() ));		
		//System.out.println("Values-1 :" + attributes.getValue("select" ));
	
		
	}
 
	public void endElement(String uri, String localName,
		String qName) throws SAXException {
		
		strQname1=qName.substring(qName.indexOf(":")+1,qName.length());
 
		if ((strQname1.equalsIgnoreCase("stylesheet") || 
				strQname1.equalsIgnoreCase("template") ||			
		        strQname1.equalsIgnoreCase("Envelope") ||
		        strQname1.equalsIgnoreCase("Header") ||		       
		        strQname1.equalsIgnoreCase("ServiceHeader") ||
		        strQname1.equalsIgnoreCase("userNameToken") ||		        
		        strQname1.equalsIgnoreCase("userName") ||
		        strQname1.equalsIgnoreCase("password") ||
		strQname1.equalsIgnoreCase("consumerID") ||
		strQname1.equalsIgnoreCase("if") ||
		strQname1.equalsIgnoreCase("when") ||
		strQname1.equalsIgnoreCase("variable") ||
		strQname1.equalsIgnoreCase("Body") ||
		strQname1.equalsIgnoreCase("choose") ||
		strQname1.equalsIgnoreCase("password"))){
		}else{
				if (tempVal!="" && tempVal!=null && !strQname.equalsIgnoreCase("value-of"))
				{	
					//System.out.println("EOPS DEFAULT VALUE:" +tempVal);	
					
					fmap.setDefaultValue(tempVal);
					//strVal=strVal+tempVal;
					if(strQname.equalsIgnoreCase("otherwise"))
					{
						fieldList.get(fieldList.size()-1).setDefaultValue(tempVal);
						System.out.println(tempVal);
					}
				}	
				
				else if (!strQname.equalsIgnoreCase("value-of")){
					//System.out.println("End Element :" +strQname1);
				}
				
		}
		if((fmap.getIsisField()!=null)){			
			if(fmap.getIsisField().length()!=0)
			{
				fieldList.add(fmap);			
				fmap=new FieldMapping();
			}
		}
		//System.out.println("End Element :" +qName.substring(qName.indexOf(":")+1,qName.length() ));			
		//System.out.println(strVal);
		
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}

	
	
}


public class ReadXMLFile {

		
	public static void readXSL(String xslFile,String mappingXlsFile)
	{
		try {

			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			XslHandler handler=new XslHandler();
		 	     
		     //ONLINE AL-ML loan creation
		     //saxParser.parse("C:\\_Test\\REQUEST-XSL\\ALML\\AL_RLS_Input.xsl", handler);
		     saxParser.parse(xslFile, handler);
		     
		     ArrayList<FieldMapping> fieldList= handler.getFieldList();
		     GenExcelData.createExcelFieldsMapping(fieldList, mappingXlsFile);
		     
		     } catch (Exception e) {
		       e.printStackTrace();
		     }
		 
		   
	}
	
	
   public static void main(String argv[]) {
 
	   ArrayList<File> files =FileFinder.getFiles("C:\\tmp\\try\\XML\\");
	   for (File file:files)
	   { 
		   String str=file.getAbsolutePath();
		   ////System.out.println(str.substring(0,str.length()-4 )); 
		   readXSL(str,	   str.substring(0,str.length()-4 )+".xls");
	   }
	   System.out.println("Completed...");
   }
 
}
