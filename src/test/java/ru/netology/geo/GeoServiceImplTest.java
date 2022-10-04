package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

// ЗАДАЧА №1, написать тесты для проверки определения локации по ip (класс GeoServiceImpl)
public class GeoServiceImplTest {

    GeoService geoService;

    @Test
    @BeforeEach
    @DisplayName("ТЕСТ, вначале создаем Mockito")
    public void creatingClassesObjects() {
        geoService = Mockito.mock(GeoServiceImpl.class);
    }

    // ЗАДАЧА №1, Проверить работу метода public Location byIp(String ip)
    @Test
    @DisplayName("ТЕСТ, проверка определения локации по IP")
    public void testDeterminesTheLocationByIp() {
        String ipAddress = "172.0.32.11"; // 96.44.183.149

        if (ipAddress.equals("172.0.32.11")) {
            // Проверка с помощью Mockito.
            Mockito.when(geoService.byIp(ipAddress)).thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));

            // Проверка с помощью JUnit.
            GeoServiceImpl geoServiceJUnit = new GeoServiceImpl();
            Location locRus = geoServiceJUnit.byIp(ipAddress); // определяем по IP адресу локацию
            Assertions.assertEquals(locRus.getCountry(), Country.RUSSIA);
            System.out.println("ТЕСТ пройден! определена локация по IP в Русском сегменте.");

        }

        if (ipAddress.equals("96.44.183.149")) {
            // Проверка с помощью Mockito.
            Mockito.when(geoService.byIp(ipAddress)).thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

            // Проверка с помощью JUnit.
            GeoServiceImpl geoServiceJUnit = new GeoServiceImpl();
            Location locUSA = geoServiceJUnit.byIp(ipAddress); // определяем по IP адресу локацию
            Assertions.assertEquals(locUSA.getCountry(), Country.RUSSIA);
            System.out.println("ТЕСТ пройден! определена локация по IP в иностранном сегменте.");
        }
    }
}
