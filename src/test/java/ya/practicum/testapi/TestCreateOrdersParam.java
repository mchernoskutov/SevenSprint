package ya.practicum.testapi;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ya.practicum.apiobjects.Orders;
import ya.practicum.apiobjects.Track;

import static ya.practicum.constants.Constants.*;

import ya.practicum.constants.Constants;

@RunWith(Parameterized.class)
public class TestCreateOrdersParam extends BaseTestSteps {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    private Orders orders;

    public TestCreateOrdersParam(String firstName, String lastName, String address, String metroStation, String phone, String rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    //данные для наполнения заказов
    @Parameterized.Parameters
    public static Object[] getOrderData() {
        return new Object[][] {
                {"Антон","Чехов","ул. Ленина, 1", "1", "8-888-999-99-00", "4", "2024-07-06", "комментарий 1", ONLY_BLACK},
                {"Андрей","Иванов","ул. Гагарина, 45б", "1", "8-888-999-99-04", "3", "2024-07-07", "комментарий 2", ONLY_GRAY},
                {"Владимир","Максимов","пр. Победы, 2", "1", "8-888-444-99-00", "2", "2024-07-12", "комментарий 3", BLACK_AND_GRAY},
                {"Николай","Петров","ул. Окружная, 673", "1", "8-888-999-22-00", "1", "2024-07-08", "комментарий 4", NO_COLOR}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI= Constants.URL;

        orders = new Orders(firstName,lastName,address,metroStation,phone,rentTime,deliveryDate,comment,color);

    }

    @Test
    @DisplayName("Create new order /api/v1/orders") // имя теста
    @Description("Проверка создания нового заказа") // описание теста
    public void postCreateNewOrder() {
        //создаем новый заказ
        Response response = postCreateNewOrder(orders);
        System.out.println(" Номер трека созданного заказа:"+response
                .body()
                .as(Track.class)
                .getTrack());
        //проверка, что трек не нулевой и ответ 201
        checkTrackOrderIsNotNull(response);
        checkStatusCodeFromResponse(response,201);
        /* проверка плохо работает, поскольку очень много повторяющихся заказов, даже если есть заказ с таким треком, все равно возвращает трек и статус 201
        //проверка, что в ответе по заказу цвет передается как отправили
        Track track = new Track(getOrderTrackAfterCreate(response));
        Assert.assertEquals(order.getColor()
                ,getOrderByTrack(track)
                        .body()
                        .as(OrderResponse.class)
                        .getOrder()
                        .getColor());
        */
    }

}
