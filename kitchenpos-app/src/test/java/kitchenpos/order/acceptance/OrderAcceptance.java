package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.http.MediaType;

import java.util.List;

public class OrderAcceptance {
    public static ExtractableResponse<Response> create_order(Long orderTableId,
            List<OrderLineItemRequest> orderLineItems) {
        OrderRequest request = new OrderRequest(orderTableId, OrderStatus.COOKING, orderLineItems);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> list() {
        return RestAssured.given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        return RestAssured.given().log().all()
                .when().put("/api/orders/" + orderId + "/order-status?request=" + orderStatus)
                .then().log().all()
                .extract();
    }
}