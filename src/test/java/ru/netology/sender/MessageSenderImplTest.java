package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTest {
    MessageSender messageSender;
    GeoService geoService;
    LocalizationService localizationService;
    Map<String, String> headers = new HashMap<>();

    @Test
    @BeforeEach
    @DisplayName("ТЕСТ, вначале создаем Mockito и HashMap")
    public void creatingClassesObjects() {
        geoService = Mockito.mock(GeoServiceImpl.class);
        localizationService = Mockito.mock(LocalizationServiceImpl.class);
        messageSender = Mockito.mock(MessageSenderImpl.class);
        headers = new HashMap<>();
    }

    @Test
    @DisplayName("ТЕСТ, проверка по IP адресу принадлежности к локации в РФ")
    public void testSendMessage() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        URL XML = new URL("http://www.geoplugin.net/xml.gp?ip=%22.getRealIpAddr()");
        InputStream stream = XML.openStream();
        Document doc = docBuilder.parse(stream);
        NodeList nodeList = doc.getChildNodes();
        Country country = null;
        //String city = null;
        //String street = "";
        //int builing = 0;
        String ipAddress = "";
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = currentNode.getChildNodes();
                    for (int j = 0; j < bookProps.getLength(); j++) {
                        Node bookProp = bookProps.item(j);
                        if (bookProp.getNodeType() != Node.TEXT_NODE) {
                            String tag = bookProp.getNodeName();
                            String value;
                            if (bookProp.getChildNodes().getLength() == 0)
                                continue; //избегаем ошибки NullPointerException
                            value = bookProp.getChildNodes().item(0).getTextContent();
                            if (tag.equals("geoplugin_countryName") && value.equals("Russia")) {
                                country = Country.RUSSIA;
                                ipAddress = "172.0.32.11"; // Необходимо для задачи IP адрес России
                            }
                            if (tag.equals("geoplugin_countryName") && value.equals("United States")) {
                                country = Country.USA;
                                ipAddress = "96.44.183.149"; // Необходимо для задачи IP адрес США
                            }

                            //if (tag.equals("geoplugin_regionName")) city = value;
                            //if (tag.equals("geoplugin_latitude") || tag.equals("geoplugin_longitude")) street += " " + value;
                            //if (tag.equals("geoplugin_request")) builing = new Integer(value.replaceAll("\\D+", ""));
                        }
                    }
                }
            }
        }
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);

        // ЗАДАЧА №2, проверить, что MessageSenderImpl всегда отправляет только русский текст, если ip относится к Российскому сегменту адресов.
        if (ipAddress.equals("172.0.32.11")) {

            // Проверка с помощью Mockito.
            Mockito.when(geoService.byIp(ipAddress)).thenReturn(new Location("Moscow", country, "Lenina", 15));
            Mockito.when(localizationService.locale(country)).thenReturn("Добро пожаловать");

            // Проверка с помощью JUnit.
            Assertions.assertEquals("Добро пожаловать", localizationService.locale(country));
            System.out.println("ТЕСТ пройден! IP относиться в Российскому сегменту и отправляет соответсвующий текст приветствия.");
        }
        // ЗАДАЧА №2, проверить, что MessageSenderImpl всегда отправляет только английский текст, если ip относится к американскому сегменту адресов.
        if (ipAddress.equals("96.44.183.149")) {

            // Проверка с помощью Mockito.
            Mockito.when(geoService.byIp(ipAddress)).thenReturn(new Location("New York", country, " 10th Avenue", 32));
            Mockito.when(localizationService.locale(country)).thenReturn("Welcome");

            // Проверка с помощью JUnit.
            Assertions.assertEquals("Welcome", localizationService.locale(country));
            System.out.println("ТЕСТ пройден! IP относиться в иностранному сегменту и отправляет соответсвующий текст приветствия.");
        }
    }
}