package kitchenpos.order.testfixtures.acceptance;

import java.util.List;
import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class OrderAcceptanceFixtures {

    private static final String BASE_URL = "/api/orders";

    public static ResponseEntity<List<OrderResponse>> 주문_전체_요청() {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<OrderResponse>>() {
            });
    }

    public static ResponseEntity<OrderResponse> 주문_등록_요청(OrderRequest orderRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.postForEntity(BASE_URL, orderRequest, OrderResponse.class);
    }

    public static ResponseEntity<OrderResponse> 주문_상태_변경_요청(Long orderId,
        OrderRequest orderRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL + "/" + orderId + "/order-status",
            HttpMethod.PUT, new HttpEntity<>(orderRequest),
            OrderResponse.class);
    }

    public static OrderRequest 주문_정의(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }
}
