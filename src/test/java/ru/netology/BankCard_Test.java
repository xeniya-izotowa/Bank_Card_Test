package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BankCard_Test {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldGetSuccessMessage() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79199199119");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetSuccessMessageWithNameWithChars() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Имя-Имя Фамилия");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79112221213");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorDueLatinCharactersInName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Name Last Name");
        driver.findElement((By.cssSelector("[data-test-id='phone'] input"))).sendKeys("+79199199119");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageDueEmptyCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Тестовое Имя");
        driver.findElement((By.cssSelector("[data-test-id='phone'] input"))).sendKeys("+79109010010");
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".checkbox__text")).getCssValue("color");
        String expected = "rgba(255, 92, 92, 1)";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageWithEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79111111111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector(".input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageWithEmptyNumber() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Новое Имя");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageWithInvalidNumber() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+абвгдеёжзий");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageIfNumberWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89881112345");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageIfNumbersSizeIsLessThan11() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+123456789");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetErrorMessageIfNumbersSizeIs12() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Фамилия Имя");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+798212345567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        assertEquals(expected, actual);
    }
}
