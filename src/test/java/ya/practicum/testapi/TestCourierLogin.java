package ya.practicum.testapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import ya.practicum.apiobjects.Courier;
import ya.practicum.apiobjects.IdCourier;
import ya.practicum.apiobjects.ResponseApi;
import ya.practicum.constants.Constants;

public class TestCourierLogin extends BaseTestSteps{

    private Courier courierAlex; //курьер Алекс, которого нет в системе
    private Courier courierBrad; //курьер Брэд, который должен быть в системе
    private Courier courierBradWrongPass; //курьер Брэд, который должен быть в системе, но пароль неправильный
    private Courier courierBradWrongLogin; //курьер Брэд, который должен быть в системе, но логин неправильный
    private Courier courierBlankPass; //курьер без пароля
    private Courier courierBlankLogin; //курьер без логина
    private Courier courierCreate; //созданный перед тестом курьер для проверки логина
    private Courier courierNoData; //курьер только с одним полем
    private IdCourier idCourier;
    private boolean isCreateCourier;


    @Before
    public void setUp() {
        RestAssured.baseURI= Constants.URL;
        //подготовка данных о курьерах
        courierAlex = new Courier("courierAlex", "1234");
        courierBrad = new Courier("courierBrad", "1234");
        courierBradWrongPass = new Courier("courierBrad", "123456");
        courierBradWrongLogin = new Courier("courierBradly", "1234");
        courierBlankPass = new Courier("courierBrad","");
        courierBlankLogin = new Courier("","1234");
        courierNoData = new Courier("courierBrad");
        courierCreate = new Courier("courierBrad", "1234", "Brad");
        isCreateCourier = false;
    }

    //курьер может авторизоваться;
    //для авторизации нужно передать все обязательные поля;
    //успешный запрос возвращает id
    @Test
    @DisplayName("Check login courier /api/v1/courier/login") // имя теста
    @Description("Проверка логина курьером, статус ответа 200") // описание теста
    public void postLoginCourier() {

        //создание курьера Брэда для проверки логина, удаляем в конце
        postCreateCourier(courierCreate);
        idCourier = postCourierLogin(courierBrad).body().as(IdCourier.class);
        isCreateCourier = true;

        //проверка логина курьера
        Response response = postCourierLogin(courierBrad);
        checkStatusCodeFromResponse(response, 200); //проверяем статус ответа
        checkIdCourierIsNotNull(response);//проверяем, что id не null
    }

    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @DisplayName("Check nonexistent courier status code 404 /api/v1/courier/login") // имя теста
    @Description("Проверка несуществующего курьера, статус ответа 404") // описание теста
    public void postLoginCourier404StatusCode() {

        Response response = postCourierLogin(courierAlex);
        checkStatusCodeFromResponse(response, 404);

    }

    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
    @Test
    @DisplayName("Check nonexistent courier message 404 /api/v1/courier/login") // имя теста
    @Description("Проверка логина несуществующего курьера, проверка ответа на запрос") // описание теста
    public void postLoginCourier404Message() {

        Response response = postCourierLogin(courierAlex);
        checkMessageFromResponse(response, "Учетная запись не найдена");
    }

    //для авторизации нужно передать все обязательные поля;
    @Test
    @DisplayName("Check not full courier data status code 400 /api/v1/courier/login") // имя теста
    @Description("Проверка неполных данных о курьере, статус ответа 400") // описание теста
    public void postLoginCourier400StatusCode() {

        Response response = postCourierLogin(courierBlankPass);
        checkStatusCodeFromResponse(response, 400);
        Response response2 = postCourierLogin(courierBlankLogin);
        checkStatusCodeFromResponse(response2, 400);
    }

    //для авторизации нужно передать все обязательные поля;
    @Test
    @DisplayName("Check not full courier data status code 400 /api/v1/courier/login") // имя теста
    @Description("Проверка неполных данных о курьере, проверка ответа на запрос") // описание теста
    public void postLoginCourier400Message() {

        Response response = postCourierLogin(courierBlankPass);
        checkMessageFromResponse(response, "Недостаточно данных для входа");
        Response response2 = postCourierLogin(courierBlankLogin);
        checkMessageFromResponse(response2, "Недостаточно данных для входа");
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @DisplayName("Check wrong courier data message /api/v1/courier/login") // имя теста
    @Description("Проверка направильных данных о курьере, проверка ответа на запрос") // описание теста
    public void postLoginCourierWrongDataMessage() {

        Response response = postCourierLogin(courierBradWrongLogin);
        checkMessageFromResponse(response, "Учетная запись не найдена");
        Response response2 = postCourierLogin(courierBradWrongPass);
        checkMessageFromResponse(response2, "Учетная запись не найдена");
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @DisplayName("Check status code 400 /api/v1/courier/login") // имя теста
    @Description("Проверка направильных данных о курьере, статус ответа 404") // описание теста
    public void postLoginCourierWrongDataStatusCode() {

        Response response = postCourierLogin(courierBradWrongLogin);
        checkStatusCodeFromResponse(response, 404);
        Response response2 = postCourierLogin(courierBradWrongPass);
        checkStatusCodeFromResponse(response2, 404);
    }

    //если какого-то поля нет, запрос возвращает ошибку 504;
    @Test
    @DisplayName("Check login courier no data status code 504 /api/v1/courier/login") // имя теста
    @Description("Проверка неполных данных о курьере только с одним полем, статус ответа 504") // описание теста
    public void postLoginCourierNoDataStatusCode() {

        Response response = postCourierLogin(courierNoData);
        checkStatusCodeFromResponse(response, 504);

    }

    @After
    public void tearDown() {
        //удаляем курьера Брэда, если он был создан
        if (isCreateCourier) {
            Assert.assertTrue("Сообщение об удалении курьера не соответствует ожидаемому",
                    deleteCourier(idCourier)
                            .body()
                            .as(ResponseApi.class)
                            .isOk());
        }
    }
}
