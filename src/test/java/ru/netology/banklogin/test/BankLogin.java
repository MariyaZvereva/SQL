package ru.netology.banklogin.test;


import org.junit.jupiter.api.*;

import ru.netology.banklogin.data.DataHelper;
import ru.netology.banklogin.data.SQLHelper;
import ru.netology.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.banklogin.data.SQLHelper.cleanAuthCodes;
import static ru.netology.banklogin.data.SQLHelper.cleanDatabase;

public class BankLogin {
    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }


    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfullyLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
       verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);

    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotification() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorNotificationWithExistUserRandomVerificationCode() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }
}
