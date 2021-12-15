package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

import static kitchenpos.AcceptanceTest.*;

public class OrderSteps {

    private static final String ORDER_URI = "/api/orders";

    public static OrderResponse 주문_등록되어_있음(OrderRequest orderRequest) {
        return 주문_등록_요청(orderRequest).as(OrderResponse.class);
    }

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return post(ORDER_URI, orderRequest);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get(ORDER_URI);
    }

    public static ExtractableResponse<Response> 주문의_주문_상태_변경_요청(Long id, OrderRequest orderRequest) {
        return put(ORDER_URI + "/{orderId}/order-status", orderRequest, id);
    }
}
