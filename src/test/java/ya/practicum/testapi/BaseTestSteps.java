package ya.practicum.testapi;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ya.practicum.apiobjects.*;
import ya.practicum.apiobjects.Track;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


public class BaseTestSteps {

    @Step("Send POST request to /api/v1/courier/login")
    public Response postCourierLogin(Courier courier){
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Compare status code from response")
    public void checkStatusCodeFromResponse(Response response, int statusCode){
        response
                .then()
                .statusCode(statusCode);
    }

    @Step("Compare message from response")
    public void checkMessageFromResponse(Response response, String message){
        response
                .then()
                .assertThat()
                .body("message",equalTo(message));
    }

    @Step("Send POST request to create courier /api/v1/courier")
    public Response postCreateCourier(Courier courier){
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
        return response;
    }

    @Step("Check if id courier after login is not null")
    public void checkIdCourierIsNotNull(Response response){
        response
                .then()
                .assertThat()
                .body("id",notNullValue());
    }

    @Step("Check if track order after create is not null")
    public void checkTrackOrderIsNotNull(Response response){
        response
                .then()
                .assertThat()
                .body("track",notNullValue());
    }

    @Step("Get order track after create")
    public String getOrderTrackAfterCreate (Response response) {
        return response
                .body()
                .as(Track.class)
                .getTrack();
    }

    @Step("Send DELETE request to delete courier /api/v1/courier")
    public Response deleteCourier(IdCourier idCourier){
        Response response = given()
                .header("Content-type", "application/json")
                .body(idCourier)
                .delete("/api/v1/courier/"+idCourier.getId());
        System.out.println("Удален курьер с ID:"+idCourier.getId());
        return response;
    }

    @Step("Check Ok from response is true")
    public boolean isMessageFromResponseOkIsTrue(Response response){
        return response
                .body()
                .as(ResponseApi.class)
                .isOk();
    }

    @Step("Create new courier")
    public void createNewCourier (Courier courier, IdCourier idCourier) {

    }

    @Step("Create new order /api/v1/orders")
    public Response postCreateNewOrder (Orders orders) {
        return given()
                .header("Content-type", "application/json")
                .body(orders)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Get order by track /api/v1/orders/track?t=")
    public Response getOrderByTrack (Track track) {
            return given()
                    .header("Content-type", "application/json")
                    .body(track)
                    .when()
                    .get("/api/v1/orders/track?t=" + track.getTrack());
    }

    @Step("Get top 10 orders /api/v1/orders?limit=10&page=0")
    public Response getTopOrders(IdCourier idCourier, int limit, int page) {
        
        return given()
                    .header("Content-type", "application/json")
                    .body(idCourier)
                    .when()
                    .get("/api/v1/orders?limit="+limit+"&page="+page);

    }

}
