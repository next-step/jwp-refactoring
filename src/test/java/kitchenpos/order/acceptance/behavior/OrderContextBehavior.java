package kitchenpos.order.acceptance.behavior;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.OrderRequest;

public class OrderContextBehavior {
    private OrderContextBehavior() {
    }
    public static ExtractableResponse<Response> 주문_추가_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(orderRequest).post("/api/orders/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문상태변경_요청(Long orderId, OrderRequest param) {
        String uri = String.format("/api/orders/%d/order-status", orderId);

        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

}
