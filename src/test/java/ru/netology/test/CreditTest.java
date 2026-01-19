package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import ru.netology.data.DBUtils;
import ru.netology.data.DataHelper;
import ru.netology.page.FormPage;
import ru.netology.data.Status;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;


public class CreditTest {
    private FormPage formPage;

    @BeforeEach
    void setUpPage() {
        String appUrl = "http://localhost";
        int appPort = 8080;
        formPage = new FormPage(appUrl + ":" + appPort);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void clearAll() {
        DBUtils.clearAllData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Payment through approved card, database check")
    void shouldPayByApprovedCardCreditStatusDB() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkCreditStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Payment through declined card, database check")
    void shouldPayByDeclinedCardInCreditStatusInDB() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateDeclinedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
        DBUtils.checkCreditStatus(Status.DECLINED);
    }

    @Test
    @DisplayName("Payment through card with 15 symbols")
    void shouldPayNotFullCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateInvalidCardNumberWithShortLength15());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through invalid card number")
    void shouldPayInvalidCardNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateInvalidCardNumberWithLetters());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Input of 17 symbols in 'Card number', string limitation")
    void shouldNotAllow17DigitsInCardNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateInvalidCardNumberWithLongLength());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through card with invalid month")
    void shouldPayInvalidMonth() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateInvalidMonth13());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Month' string")
    void shouldPayInvalidMonth00() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateInvalidMonthZero());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with invalid year")
    void shouldPayInvalidYear() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateExpiredYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Year' string")
    void shouldPayInvalidYear00() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateInvalidYearZero());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with cyrillic symbols in 'Owner' string")
    void shouldPayCyrillicCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateInvalidOwnerWithCyrillicSymbols());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through card with invalid owner")
    void shouldPayInvalidCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateInvalidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Input of 4 symbols in 'CVV', string limitation")
    void shouldNotAllow4DigitsInCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateInvalidCVVWith4Digit());
        // Смотрим автоматическую обрезку 4-го символа и успешность отправки формы
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Input of 2 symbols in 'CVV', string limitation")
    void shouldNotAllow2DigitsInCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateInvalidCVVWith2Digit());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Card number' string")
    void shouldPayEmptyCardNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateEmptyCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Month' string")
    void shouldPayEmptyCardMonth() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateEmptyString());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Year' string")
    void shouldPayEmptyCardYear() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateEmptyString());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Owner' string")
    void shouldPayEmptyCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateEmptyString());
        formPage.setCardCVV(DataHelper.generateValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Payment through empty 'CVV' string")
    void shouldPayEmptyCardCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateMonth());
        formPage.setCardYear(DataHelper.generateYear());
        formPage.setCardOwner(DataHelper.generateValidOwner());
        formPage.setCardCVV(DataHelper.generateEmptyString());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }
}