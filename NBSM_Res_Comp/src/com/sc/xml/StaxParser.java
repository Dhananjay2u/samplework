package com.sc.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

class Item
{
	String id=null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}

public class StaxParser {
    static final String MESSAGE = "Message";
    static final String ID = "id";


    
    public List<Item> readConfig(String configFile) {
    	
            List<Item> items = new ArrayList<Item>();
            try {
                    //  create a new XMLInputFactory
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    // Setup a new eventReader
                    InputStream in = new FileInputStream(configFile);
                    XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                    // read the XML document
                    Item item = null;

                    while (eventReader.hasNext()) {
                            XMLEvent event = eventReader.nextEvent();

                            if (event.isStartElement()) {
                                    StartElement startElement = event.asStartElement();
                                    if (startElement.getName().getLocalPart().equals(MESSAGE)) {
                                    	
                                    	item=new Item();
                                    	Iterator<Attribute> attributes = startElement
                                                .getAttributes();
                                    	//startElement.get
                                    	 while (attributes.hasNext()) {
                                             Attribute attribute = attributes.next();
                                             if (attribute.getName().toString().equals(ID)) {
                                                     item.setId(attribute.getValue());
                                             }

                                     }
                                    	
                                    }
                                    
                                  
                            }
                            // If we reach the end of an item element, we add it to the list
                            if (event.isEndElement()) {
                                    EndElement endElement = event.asEndElement();
                                    if (endElement.getName().getLocalPart().equals("Message")) {
                                            items.add(item);
                                    }
                            }

                    }
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
            } catch (XMLStreamException e) {
                    e.printStackTrace();
            }
            return items;
    }
    
    
    public static void main(String args[])
    {
    	
    	StaxParser pars=new StaxParser();
    	 List<Item> list= pars.readConfig("C:\\DK\\DK\\Work\\28-apr-2017\\MessageConfig.xml");
    	 
    	 for(Item m:list)
    	 {
    		 System.out.println("id=\""+m.getId()+"\"");
    	 }
    }

}