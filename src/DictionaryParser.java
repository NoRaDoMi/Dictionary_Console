/**
 * Created by Asus on 5/4/2020.
 */


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Asus on 5/4/2020.
 */
public class DictionaryParser extends DefaultHandler {

    Map<String,String> dictionary = new LinkedHashMap<>();

    boolean isWord = false;
    boolean isMeaning = false;
    String word = "";

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("word")) {
            isWord = true;
        } else if (qName.equalsIgnoreCase("meaning")) {
            isMeaning = true;
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("word")) {
            isWord = false;
        } else if (qName.equalsIgnoreCase("meaning")) {
            isMeaning = false;
        }
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch,start,length);

        if(isWord){
            dictionary.put(value,"");
            word = value;
            isWord = false;
        } else if(isMeaning){
            dictionary.replace(word,dictionary.get(word) + value);
//            System.out.println(currRecord);
        }
    }

    public void display(){
        System.out.println("Danh sach TU DIEN: ");
        dictionary.forEach((k,v)->{
            System.out.println(k+" - "+v);
        });
    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }
}



