package ru.netology.test;


import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getFirstCardInfo;
import static ru.netology.data.DataHelper.getSecondCardInfo;


public class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecond() {
        var dashboardPage = new DashboardPage();
        var balanceFirstBefore = dashboardPage.getCardBalance1();
        var balanceSecondBefore = dashboardPage.getCardBalance2();
        int amount = 1200;
        var verification = dashboardPage.personSecondCard();
        verification.transferMoney(amount, getFirstCardInfo());
        var balanceFirstAfter = balanceFirstBefore - amount;
        var balanceSecondAfter = balanceSecondBefore + amount;

        assertEquals(balanceFirstAfter, dashboardPage.getCardBalance1());
        assertEquals(balanceSecondAfter, dashboardPage.getCardBalance2());
    }
    @Test
    void shouldTransferMoneyFromSecondToFirst() {
        var dashboardPage = new DashboardPage();
        var balanceFirstBefore = dashboardPage.getCardBalance1();
        var balanceSecondBefore = dashboardPage.getCardBalance2();
        int amount = 2000;
        var verification = dashboardPage.personFirstCard();
        verification.transferMoney(amount, getSecondCardInfo());
        var balanceFirstAfter = balanceFirstBefore + amount;
        var balanceSecondAfter = balanceSecondBefore - amount;

        assertEquals(balanceFirstAfter, dashboardPage.getCardBalance1());
        assertEquals(balanceSecondAfter, dashboardPage.getCardBalance2());
    }

    @Test
    void shouldNotTransferMoreMoneyThanItHas() {
        var dashboardPage = new DashboardPage();

        int amount = 32000;
        var verification = dashboardPage.personFirstCard();
        verification.transferMoney(amount, getSecondCardInfo());
        verification.getError();

    }


}