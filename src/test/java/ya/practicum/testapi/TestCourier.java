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
import ya.practicum.constants.Constants;
import ya.practicum.apiobjects.ResponseApi;

public class TestCourier extends BaseTestSteps {


    private Courier courierBrad;
    private Courier courierBradCreate;
    private Courier courierBradCreateNoPassword;
    private Courier courierBradCreateNoLogin;
    private IdCourier idCourier;
    private boolean isCreateCourier;



    @Before
    public void setUp() {
        RestAssured.baseURI= Constants.URL;
        //подготовка курьеров
        courierBrad = new Courier("courierBrad", "1234");
        courierBradCreate = new Courier("courierBrad", "1234", "Brad");
        courierBradCreateNoPassword = new Courier("courierBrad", "", "Brad");
        courierBradCreateNoLogin = new Courier("", "1234", "Brad");
        isCreateCourier = false;

    }

    //курьера можно создать;
    //запрос возвращает правильный код ответа;
    //успешный запрос возвращает ok: true;
    @Test
    @DisplayName("Create new courier /api/v1/courier") // имя теста
    @Description("Создание нового курьера, код ответа 201") // описание теста
    public void postCreateNewCourier() {

        //создание нового курьера
        Response response = postCreateCourier(courierBradCreate);
        idCourier = postCourierLogin(courierBradCreate).body().as(IdCourier.class);
        isCreateCourier = true;
        //проверки статуса ответа и сообщения ok:true
        checkStatusCodeFromResponse(response, 201);
        Assert.assertTrue(isMessageFromResponseOkIsTrue(response));
        //проверка, что новый созданный курьер может залогиниться
        Response response2 = postCourierLogin(courierBrad);
        checkIdCourierIsNotNull(response2);

    }

    //нельзя создать двух одинаковых курьеров;
    //если создать пользователя с логином, который уже есть, возвращается ошибка.
    @Test
    @DisplayName("Create double courier status code 409 /api/v1/courier") // имя теста
    @Description("Попытка создания уже существующего курьера, код ответа 409") // описание теста
    public void postCreateNewCourierDoubleStatusCode409() {

        //создание нового курьера
        Response response = postCreateCourier(courierBradCreate);
        idCourier = postCourierLogin(courierBradCreate).body().as(IdCourier.class);
        isCreateCourier = true;
        //проверки
        checkStatusCodeFromResponse(response, 201);
        Assert.assertTrue(isMessageFromResponseOkIsTrue(response));

        //повторная попытка создания курьера
        Response response2 = postCreateCourier(courierBradCreate);
        checkStatusCodeFromResponse(response2, 409);

    }

    //нельзя создать двух одинаковых курьеров;
    //если создать пользователя с логином, который уже есть, возвращается ошибка.
    @Test
    @DisplayName("Create double courier message /api/v1/courier") // имя теста
    @Description("Попытка создания уже существующего курьера, проверка сообщения") // описание теста
    public void postCreateNewCourierDoubleMessage() {

        //создание нового курьера
        Response response = postCreateCourier(courierBradCreate);
        idCourier = postCourierLogin(courierBradCreate).body().as(IdCourier.class);
        isCreateCourier = true;
        //проверки
        checkStatusCodeFromResponse(response, 201);
        Assert.assertTrue(isMessageFromResponseOkIsTrue(response));

        //повторная попытка создания курьера
        Response response2 = postCreateCourier(courierBradCreate);
        //сообщение отличается от сообщения в документации, оставлено фактическое
        checkMessageFromResponse(response2, "Этот логин уже используется. Попробуйте другой.");

    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @Test
    @DisplayName("Create courier with no login or password status 400/api/v1/courier") // имя теста
    @Description("Попытка создания курьера без логина или пароля, проверка статуса") // описание теста
    public void postCreateNewCourierNotFullDataStatusCode() {

        Response response = postCreateCourier(courierBradCreateNoLogin);

        checkStatusCodeFromResponse(response, 400);

        Response response2 = postCreateCourier(courierBradCreateNoPassword);

        checkStatusCodeFromResponse(response2, 400);

    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @Test
    @DisplayName("Create courier with no login or password message/api/v1/courier") // имя теста
    @Description("Попытка создания курьера без логина или пароля, проверка сообщения") // описание теста
    public void postCreateNewCourierNotFullDataMessage() {

        Response response = postCreateCourier(courierBradCreateNoLogin);

        checkMessageFromResponse(response, "Недостаточно данных для создания учетной записи");

        Response response2 = postCreateCourier(courierBradCreateNoPassword);

        checkMessageFromResponse(response2, "Недостаточно данных для создания учетной записи");

    }

    @After
    public void tearDown() {
        //удаляем курьера Брэда, если он был создан
        if (isCreateCourier) {
            Assert.assertTrue("Сообщение об удалении курьера не соответствует ожидаемому, возможно, курьер с id:"+ idCourier.getId()+" не удален после теста",
                    deleteCourier(idCourier)
                            .body()
                            .as(ResponseApi.class)
                            .isOk());
        }

    }

}
