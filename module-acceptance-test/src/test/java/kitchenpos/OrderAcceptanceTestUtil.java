package kitchenpos;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static kitchenpos.AcceptanceTest.restTemplate;
import static org.assertj.core.api.Assertions.assertThat;

public final class OrderAcceptanceTestUtil {

    private OrderAcceptanceTestUtil() {
    }

    public static OrderResponse 주문_등록됨(OrderTableResponse orderTable, MenuResponse... menus) {
        return 주문_생성_요청(orderTable, menus).getBody();
    }

    static ResponseEntity<OrderResponse> 주문_생성_요청(OrderTableResponse orderTable, MenuResponse... menus) {
        return 주문_생성_요청(orderTable, toOrderLineItems(menus));
    }

    static ResponseEntity<OrderResponse> 주문_생성_요청(OrderTableResponse orderTable,
                                                         List<OrderLineItemRequest> items) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTableId", orderTable.getId());
        request.put("orderLineItems", items);
        return restTemplate().postForEntity("/api/orders", request, OrderResponse.class);
    }

    private static List<OrderLineItemRequest> toOrderLineItems(MenuResponse[] menus) {
        return Arrays.stream(menus)
                     .map(m -> new OrderLineItemRequest(m.getId(), 1L))
                     .collect(Collectors.toList());
    }

    static ResponseEntity<List<OrderResponse>> 주문_목록_조회_요청() {
        return restTemplate().exchange("/api/orders", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<OrderResponse>>() {});
    }

    static ResponseEntity<OrderResponse> 주문_상태_변경_요청(OrderResponse order, OrderStatus status) {
        return 주문_상태_변경_요청(order.getId(), status);
    }

    static ResponseEntity<OrderResponse> 주문_상태_변경_요청(Long orderId, OrderStatus status) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("orderId", orderId);

        Map<String, Object> request = new HashMap<>();
        request.put("orderStatus", status.name());
        return restTemplate().exchange("/api/orders/{orderId}/order-status", HttpMethod.PUT,
                                       new HttpEntity<>(request), OrderResponse.class, urlVariables);
    }

    static void 주문_생성됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    static void 주문_생성시_조리상태_확인(ResponseEntity<OrderResponse> response) {
        OrderResponse order = response.getBody();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    static void 주문_생성_실패됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 주문_목록_응답됨(ResponseEntity<List<OrderResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 주문_상태_변경됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    static void 주문_상태_변경_실패됨(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 주문_목록_주문에_주문_항목이_포함됨(ResponseEntity<List<OrderResponse>> response,
                                            MenuResponse... menus) {
        List<Long> menuIds = response.getBody()
                                     .stream()
                                     .flatMap(o -> o.getOrderLineItems().stream())
                                     .map(OrderLineItemResponse::getMenuId)
                                     .collect(Collectors.toList());
        List<Long> expectedIds = Arrays.stream(menus)
                                       .map(MenuResponse::getId)
                                       .collect(Collectors.toList());
        assertThat(menuIds).containsExactlyElementsOf(expectedIds);
    }
}
