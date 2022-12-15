package kitchenpos.Acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.TestFixture;
import kitchenpos.domain.Order;

public class OrderTestFixture extends TestFixture {

    public static final String ORDER_BASE_URI = "/api/orders";

    public static ExtractableResponse<Response> 주문_생성_요청함(Order order) {
        return post(ORDER_BASE_URI, order);
    }

    public static ExtractableResponse<Response> 주문_조회_요청함() {
        return get(ORDER_BASE_URI);
    }

    public static ExtractableResponse<Response> 주문_상태변경_요청함(Long orderId, Order order) {
        return put(ORDER_BASE_URI + "/" + orderId + "/order-status", order);
    }


    public static void 주문_요청_응답됨(ExtractableResponse<Response> response) {
        ok(response);
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        created(response);
    }

    public static void 주문_조회_포함됨(ExtractableResponse<Response> response,
                                 List<ExtractableResponse<Response>> orderResponses) {
        List<Long> actualIds = response.jsonPath()
                .getList(".", Order.class)
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        List<Long> expectIds = orderResponses.stream()
                .map(r -> r.as(Order.class))
                .collect(Collectors.toList())
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectIds);
    }

    public static void 주문_상태_변경됨(Order order, Order status) {
        assertThat(order.getOrderStatus()).isEqualTo(status.getOrderStatus());
    }
}
