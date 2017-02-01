package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import eg.edu.alexu.csd.oop.dbms.query.Insert;

public class XML implements IFileBehaviour {

    private Table operand;
    private String tableName;
    private Element rootElement;

    public XML() {

    }

    @Override
    public void setTable(Table table) {
        operand = table;
        tableName = operand.getTableName();
    }

    @Override
    public Table getTable() {
        return operand;
    }

    @Override
    public void saveTable(File file) {
        tableName = operand.getTableName();
        file = new File(file, tableName + ".xml");
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
             System.out.println("saving "+tableName);
            rootElement = doc.createElement(tableName);
            for (int cnt = 0; cnt < operand.getAttributes().size(); cnt++) {

                rootElement.setAttribute(operand.getAttributes().get(cnt),
                        operand.getTypeOf(operand.getAttributes().get(cnt)));
            }

            doc.appendChild(rootElement);

            for (int i = 0; i < operand.getColumn(0).size(); i++) {
                // className elements

                Element element = doc.createElement("row");
                rootElement.appendChild(element);

                for (int j = 0; j < operand.getAttributes().size(); j++) {

                    Object data = operand.getColumn(operand.getAttributes().get(
                            j))
                            .get(i);
                    String s;
                    if (data == null) {
                        s = "null";
                    } else {
                        s = operand.getColumn(operand.getAttributes().get(j))
                                .get(i).toString();
                    }
                    Element className = doc.createElement(operand
                            .getAttributes().get(j));
                    className.appendChild(doc.createTextNode(s));
                    element.appendChild(className);
                }

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMImplementation domImpl = doc.getImplementation();
            DocumentType doctype = domImpl.createDocumentType("doctype", "",
                    tableName + ".dtd");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype
                    .getSystemId());
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> getTypes() {
        NamedNodeMap p = rootElement.getAttributes();
        HashMap<String, String> toreturn = new HashMap<String, String>();
        for (int i = 0; i < p.getLength(); i++) {
            Node n = p.item(i);
            if (n.getNodeValue().equals("varchar"))
                toreturn.put(n.getNodeName(), "varchar");
            else if (n.getNodeValue().equals("int"))
                toreturn.put(n.getNodeName(), "int");
            else if (n.getNodeValue().equals("float"))
                toreturn.put(n.getNodeName(), "float");
            else if (n.getNodeValue().equals("date"))
                toreturn.put(n.getNodeName(), "date");

        }
        return toreturn;

    }

    @Override
    public void loadTable(File file) {

        Table table;

        try {

            File fXmlFile = file;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            dbFactory.setValidating(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dBuilder.setErrorHandler(new ErrorHandler() {

                @Override
                public void warning(SAXParseException exception)
                        throws SAXException {
                    exception.getStackTrace();
                }

                @Override
                public void fatalError(SAXParseException exception)
                        throws SAXException {
                    exception.getStackTrace();
                }

                @Override
                public void error(SAXParseException exception)
                        throws SAXException {
                    exception.getStackTrace();
                }
            });
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            String str = file.getName().replace(".xml", "").trim();
            rootElement = (Element) doc.getElementsByTagName(str).item(0);
            
            NodeList nList = doc.getElementsByTagName("row");

            List<String> attributes = new ArrayList<String>();
            HashMap<String, String> hmap = getTypes();
            table = new Table(hmap);
            table.orderatts(attributes);
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node sNode = nList.item(temp);

                HashMap<String, Object> data = new HashMap<String, Object>();
                for (int j = 0; j < sNode.getChildNodes().getLength(); j++) {

                    Node nNode = sNode.getChildNodes().item(j);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        String elementName = eElement.getTextContent();

                        data.put(eElement.getTagName(), Insert.parseType(
                                elementName,
                                table.getTypeOf(eElement.getTagName())));
                        
                    }
                }
                table.insertRowSure(data);
            }

            operand = table;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveTableSchema(File file) {
        file = new File(file, operand.getTableName() + ".dtd");
        try {

            FileWriter writer = new FileWriter(file);
            if (operand.getColumn(operand.getAttributes().get(0)).size() == 0) {

                writer.write("<!ELEMENT " + tableName + " (row)>" + System
                        .getProperty("line.separator"));
                writer.write("<!ELEMENT row (");
                for (int i = 0; i < operand.getAttributes().size(); i++) {
                    if (i != operand.getAttributes().size() - 1) {
                        writer.write(operand.getAttributes().get(i) + ",");
                    } else {
                        writer.write(operand.getAttributes().get(i) + ")>"
                                + System.getProperty("line.separator"));
                    }
                }

            } else {
                writer.write("<!ELEMENT " + tableName + " (row)>" + System
                        .getProperty("line.separator"));
                writer.write("<!ELEMENT row (");
                for (int i = 0; i < operand.getAttributes().size(); i++) {
                    if (i != operand.getAttributes().size() - 1) {
                        writer.write(operand.getAttributes().get(i) + ",");
                    } else {
                        writer.write(operand.getAttributes().get(i) + ")>"
                                + System.getProperty("line.separator"));
                    }
                }
                for (int j = 0; j < operand.getAttributes().size(); j++) {
                    writer.write(
                            "<!ELEMENT " + operand.getAttributes().get(j)
                                    + " (#PCDATA)>" + System.getProperty(
                                            "line.separator"));
                }
            }
            writer.close();

        } catch (Exception e) {
            System.out.println("error");
        }

    }

    @Override
    public String getExtension() {
      return "xml";
    }

}
