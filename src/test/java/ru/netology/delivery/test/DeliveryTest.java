package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        Configuration.browserSize = "1000x800";
        open("http://localhost:9999");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a"),
                Keys.DELETE);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $x("//input[@placeholder='Дата встречи']").setValue(firstMeetingDate);
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $x("//span[@class='checkbox__box']").click();
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']").shouldBe(visible, Duration.ofSeconds(15));
        $x("//div[@class='notification__content']").shouldHave(text("Встреча успешно запланирована на "
                + firstMeetingDate));
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a"),
                Keys.DELETE);
        $x("//input[@placeholder='Дата встречи']").setValue(secondMeetingDate);
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']").shouldBe(visible, Duration.ofSeconds(15));
        $x("//div[@class='notification notification_visible notification_status_error " +
                "notification_has-closer notification_stick-to_right notification_theme_alfa-on-white']//div[3]")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).click();
        $x("//div[@class='notification__content']").shouldHave(text("Встреча успешно запланирована на "
                + secondMeetingDate));
    }
}