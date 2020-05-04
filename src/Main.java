import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Asus on 5/4/2020.
 */
public class Main {

    static Map<String,String> dictionary = new LinkedHashMap<>();
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws Exception {

        printHeader();
        dictionary = loadData("AnhViet.xml").getDictionary();

//        System.out.println("Kich thuoc tu dien : "+dictionary.size());
//
//        System.out.println("Tra tu home: "+dictionary.get("home"));

        int choose;
        do {
            showMenu();
            System.out.println();
            choose = getChoose();
            switch (choose) {
                case 1:{
                    break;
                }
                case 2:{
                    traCuu();
                    break;
                }
                case 3:{
                    addWord();
                    break;
                }

                case 4:{
                    deleteWord();
                    break;
                }
                default: break;
            }
        } while (choose != 0);
    }

//    Chức năng 1
    static void traCuu(){
        System.out.println("2. TRA CUU");
        System.out.print("Nhap tu: ");
        String key = scanner.nextLine();
        if(dictionary.containsKey(key)){
            System.out.println("Kết quả: ");
            System.out.println(dictionary.get(key));
        }
        else{
            System.out.println("Du lieu khong ton tai.");
        }
    }

//    Chức năng 2
    static void addWord() throws Exception{
        String key, value;
        System.out.println("3. THEM TU MOI");

        System.out.print("Nhap tu: ");
        key = scanner.nextLine();

        System.out.print("Nhap nghia: ");
        value = scanner.nextLine();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);

        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();

        Document doc = db.parse(new FileInputStream(new File("AnhViet.xml")));

        Element element = doc.getDocumentElement();

        Element textNode = doc.createElement("word");
        textNode.setTextContent(key);
        Element textNode1 = doc.createElement("meaning");
        textNode1.setTextContent(value);
        Element nodeElement = doc.createElement("record");
        nodeElement.appendChild(textNode);
        nodeElement.appendChild(textNode1);
        element.appendChild(nodeElement);
        doc.replaceChild(element, element);
        Transformer tFormer =
                TransformerFactory.newInstance().newTransformer();
        tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
        Source source = new DOMSource(doc);
        Result result = new StreamResult("AnhViet.xml");
        tFormer.transform(source, result);

        System.out.println("Done");
    }

//    Chức năng 3
    static void deleteWord() throws Exception{
        System.out.println("4. XOA MOT TU: ");
        String key ;
        int index;
        while(true){
            System.out.print("Nhap tu can xoa: ");
            key = scanner.nextLine();
            if(dictionary.containsKey(key)) {
//                Xóa dữ liệu trong dictionary
                index = getIndexOfWord(key);
                dictionary.remove(key);
                break;
            }
            else{
                System.out.println("Tu vua nhap khong ton tai. Vui long nhap lai");
            }
        }

//        Xóa dữ liệu trong file xml
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        File file = new File("AnhViet.xml");
        Document doc = db.parse(new FileInputStream(file));

        Element element = (Element) doc.getElementsByTagName("record").item(index);

        Node parent = element.getParentNode();
        parent.removeChild(element);
        parent.normalize();

        Transformer tFormer =
                TransformerFactory.newInstance().newTransformer();
        tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
        Source source = new DOMSource(doc);
        Result result = new StreamResult("AnhViet.xml");
        tFormer.transform(source, result);

        System.out.println("> Da xoa "+key+" khoi tu dien.");
//        System.out.println(element.getTextContent());
    }

    static int getIndexOfWord(String key){
        List<String>  listKeys = new ArrayList<>(dictionary.keySet());
        return listKeys.indexOf(key);
    }
    static void printHeader(){
        System.out.println("     +========================================+");
        System.out.println("     ||             TỪ ĐIỂN CỦA TÔI          ||");
        System.out.println("     +=======================================+");
    }

    static void showMenu() {
        System.out.println("+--------- Vui long chon mot chuc nang: -------------+");
        System.out.println("|   1) Chuyen doi ngon ngu                           |");
        System.out.println("|   2) Tra cuu tu                                    |");
        System.out.println("|   3) Them tu moi                                   |");
        System.out.println("|   4) Xoa mot tu                                    |");
        System.out.println("|   5) Thong ke tan suat                             |");
        System.out.println("|   0) Thoat                                         |");
        System.out.println("+----------------------------------------------------+");
    }

    static int getChoose(){
        int choose = -1;
        while(choose < 0 || choose > 5){
            try {
                System.out.print("Chon chuc nang (0 -> 6): ");
                choose = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Try again.");
            }

        }
        return choose;
    }

    static DictionaryParser loadData(String filename) throws IOException, SAXException, ParserConfigurationException{
        FileInputStream f = new FileInputStream(filename);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DictionaryParser dictionaryParser = new DictionaryParser();
        saxParser.parse(f, dictionaryParser);

        return dictionaryParser;
//        dictionaryParser.display();
    }
}
