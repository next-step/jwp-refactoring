package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRestAssured {
    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId,
                                                             List<OrderLineItemRequest> orderLineItems) {
        OrderRequest request = new OrderRequest(orderTableId, OrderStatus.COOKING, orderLineItems);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_수정_요청(Long orderId, OrderStatus orderStatus) {
        return RestAssured.given().log().all()
                .when().put("/api/orders/" + orderId + "/order-status?request=" + orderStatus)
                .then().log().all()
                .extract();
    }
}
