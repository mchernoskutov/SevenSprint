package ya.practicum.testapi;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ya.practicum.apiobjects.*;
import ya.practicum.constants.Constants;

public class TestGetOrders extends BaseTestSteps{

    private OrderResponse orderList;

    @Before
    public void setUp () {
       RestAssured.baseURI= Constants.URL;
    }

    @Test
    @DisplayName("Get order by nonexistent id /api/v1/orders/track?t=") // имя теста
    @Description("Проверка получения заказа без указания id трекера") // описание теста
    public void getOrderByNonexistentId() {

        //Создаем новый трек с несуществующим значением
        Track track = new Track("5000001");
        //Запрос с номером трека
        Response response = getOrderByTrack(track);
        //проверка сообщения
        checkMessageFromResponse(response,"Заказ не найден");
        //проверка кода ответа
        checkStatusCodeFromResponse(response,404);

    }

    @Test
    @DisplayName("Get order by nonexistent id /api/v1/orders/track?t=") // имя теста
    @Description("Проверка получения заказа без указания id трекера") // описание теста
    public void getOrderByNullId() {

        //Создаем новый трек с пустым значением
        Track track = new Track("");
        //Запрос с номером трека
        Response response = getOrderByTrack(track);
        //проверка сообщения
        checkMessageFromResponse(response,"Недостаточно данных для поиска");
        //проверка кода ответа
        checkStatusCodeFromResponse(response,400);

    }

    @Test
    @DisplayName("Get available 10 orders /api/v1/orders/") // имя теста
    @Description("Проверка получения 10 доступных заказов для курьера") // описание теста
    public void getTopOrders() {
        IdCourier idCourier = new IdCourier("");
        int limit = 10;
        int page = 0;
        orderList = getTopOrders(idCourier, limit, page).body().as(OrderResponse.class);

        Assert.assertEquals(Integer.toString(limit),orderList.getPageInfo().getLimit());
        Assert.assertEquals(Integer.toString(page),orderList.getPageInfo().getPage());
        //проверка, что получено 10 заказов
        Assert.assertEquals(limit,orderList.getOrder().size());

    }


}
