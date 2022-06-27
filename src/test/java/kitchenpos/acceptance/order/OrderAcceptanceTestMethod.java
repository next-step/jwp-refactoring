package kitchenpos.acceptance.order;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTestMethod extends AcceptanceTest {

    private static final String ORDER_PATH = "/api/orders";
    private static final String ORDER_STATUS_CHANGE_PATH_FORMAT = "/api/orders/%s/order-status";
    private static final String DOT = ".";

    public static ExtractableResponse<Response> 주문_등록_요청(Order params) {
        return post(ORDER_PATH, params);
    }

    public static ExtractableResponse<Response> 주문_등록되어_있음(Order params) {
        return 주문_등록_요청(params);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long id, Order params) {
        return put(String.format(ORDER_STATUS_CHANGE_PATH_FORMAT, id), params);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get(ORDER_PATH);
    }

    public static void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(parseURIFromLocationHeader(response)).isNotBlank();

        Order orderByResponse = response.as(Order.class);
        assertThat(orderByResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, String orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Order orderByResponse = response.as(Order.class);
        assertThat(orderByResponse.getOrderStatus()).isEqualTo(orderStatus);
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response,
                                    List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedIds = createdResponses.stream()
                .map(AcceptanceTest::parseIdFromLocationHeader)
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList(DOT, Order.class)
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectedIds);
    }
}