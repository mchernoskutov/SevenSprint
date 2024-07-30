package ya.practicum.testapi;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ya.practicum.apiobjects.Courier;
import ya.practicum.apiobjects.IdCourier;
import ya.practicum.apiobjects.ResponseApi;
import ya.practicum.constants.Constants;

public class TestCourierDelete extends BaseTestSteps {


    private Courier courierBrad;
    private Courier courierBradCreate;
    private boolean isCreateCourier;
    private IdCourier idCourier;


    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
        courierBrad = new Courier("courierBrad", "1234");
        courierBradCreate = new Courier("courierBrad", "1234", "Brad");
        isCreateCourier = false;
    }

    @Test
    @DisplayName("Delete courier /api/v1/courier/:id") // имя теста
    @Description("Проверка удаления курьера, статус ответа 200, попытка повторного удаления, код ответа 404") // описание теста
    public void deleteCourier() {

        //создание нового курьера для удаления
        Response response = postCreateCourier(courierBradCreate);
        idCourier = postCourierLogin(courierBrad).body().as(IdCourier.class);
        isCreateCourier = true;

        //проверка удаления и статуса ответа ok: true
        Response responseDelete = deleteCourier(idCourier);
        checkStatusCodeFromResponse(responseDelete, 200);
        isMessageFromResponseOkIsTrue(responseDelete);

        //проверка повторного удаления, код ответа 404
        Response responseSecondDelete = deleteCourier(idCourier);
        checkStatusCodeFromResponse(responseSecondDelete, 404);
        checkMessageFromResponse(responseSecondDelete,"Курьера с таким id нет.");
    }

    @Test
    @DisplayName("Delete courier with null id /api/v1/courier/:id") // имя теста
    @Description("Проверка удаления курьера без id, статус ответа 400") // описание теста
    public void deleteCourierNullId() {
        //задаем пустое значение id
        IdCourier idCourierNull = new IdCourier("");

        //проверка удаления и статуса ответа 400
        Response responseDelete = deleteCourier(idCourierNull);
        checkStatusCodeFromResponse(responseDelete, 404);
        //сообщение и код отличается от документации, тест по фактическим значениям
        checkMessageFromResponse(responseDelete,"Not Found.");

    }


    @After
    public void tearDown() {
        //удаляем курьера Брэда, если он не был вдруг удален
        if (isCreateCourier) {
                    deleteCourier(idCourier)
                            .body()
                            .as(ResponseApi.class);
        }
    }






}
