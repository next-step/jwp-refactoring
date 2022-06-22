package kitchenpos.acceptance;

import static kitchenpos.utils.DomainFixtureFactory.createOrder;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import org.springframework.http.MediaType;

public class OrderRestAssured {
    public static ExtractableResponse<Response> 주문_등록_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Order targetOrder, String orderStatus) {
        Order order = createOrder(1L, null, orderStatus, null, null);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/{orderId}/order-status", targetOrder.getId())
                .then().log().all()
                .extract();
    }
}
