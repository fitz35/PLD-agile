package Model;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Map {
    private ArrayList<Segment> segmentList;
    private ArrayList<Intersection> intersectionList;

    public Map() {
        segmentList = new ArrayList<Segment>();
        intersectionList = new ArrayList<Intersection>();
    }

    public void loadMap(String fileName)
    {
        //Test well formed XML file name
        String[] words = fileName.split(".");
        assert(words[(words.length)-1].equals("XML") || words[(words.length)-1].equals("xml"));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(fileName));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            // get <Intersection>
            NodeList nodeListIntersection = doc.getElementsByTagName("intersection");

            for (int temp = 0; temp < nodeListIntersection.getLength(); temp++) {
                Node node = nodeListIntersection.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get intersection's attribute
                    long id =  Long.parseLong(element.getAttribute("id"));
                    double latitude = Double.parseDouble(element.getAttribute("latitude"));
                    double longitude = Double.parseDouble(element.getAttribute("longitude"));

                    intersectionList.add(new Intersection(id,latitude,longitude));

                    System.out.printf("out ");
                }
            }

            // get <Segment>
            NodeList nodeListSegment = doc.getElementsByTagName("segment");

            for (int temp = 0; temp < nodeListSegment.getLength(); temp++) {
                Node node = nodeListSegment.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get intersection's attribute
                    long destinationId =  Long.parseLong(element.getAttribute("destination"));
                    long originId =  Long.parseLong(element.getAttribute("origin"));
                    double length = Double.parseDouble(element.getAttribute("length"));
                    String name = element.getAttribute("name");

                    /*
                    Intersection origin = getIntersectionById(originId);
                    Intersection destination = getIntersectionById(destinationId);
                     */

                    intersectionList.add(new Intersection(id,latitude,longitude));

                    System.out.printf("out ");
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }



    }

}
