package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;

// ЗАДАЧА №1, написать тесты для проверки возвращаемого текста (класс LocalizationServiceImpl).
public class LocalizationServiceImplTest {
    LocalizationService localizationService;

    @Test
    @BeforeEach
    @DisplayName("ТЕСТ, вначале  создаем Mockito")
    public void creatingClassesObjects() {
        localizationService = Mockito.mock(LocalizationServiceImpl.class);
    }

    // ЗАДАЧА №1, Проверить работу метода public String locale(Country country)
    @Test
    @DisplayName("ТЕСТ, проверка возвращаемого текста")
    public void checkingTheReturnedText() {
        String ipAddress = "172.0.32.11"; // 96.44.183.149

        if (ipAddress.equals("172.0.32.11")) {
            // Проверка с помощью Mockito.
            Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

            // Проверка с помощью JUnit.
            LocalizationServiceImpl localizationServiceImpl = new LocalizationServiceImpl();
            Assertions.assertEquals(localizationServiceImpl.locale(Country.RUSSIA), "Добро пожаловать");
            System.out.println("ТЕСТ пройден! Проверка на возврат текста на Русском языке.");
        }

        if (ipAddress.equals("96.44.183.149")) {
            // Проверка с помощью Mockito.
            Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

            // Проверка с помощью JUnit.
            LocalizationServiceImpl localizationServiceImpl = new LocalizationServiceImpl();
            Assertions.assertEquals(localizationServiceImpl.locale(Country.USA), "Welcome");
            System.out.println("ТЕСТ пройден! Проверка на возврат текста на иностранном языке.");
        }
    }
}
