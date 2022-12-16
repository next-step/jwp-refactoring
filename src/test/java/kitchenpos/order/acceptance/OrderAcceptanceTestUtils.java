package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderTestFixture.orderRequest;
import static kitchenpos.order.domain.OrderTestFixture.orderStatusRequest;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTestUtils {
    private static final String ORDER_PATH = "/api/orders";

    private OrderAcceptanceTestUtils() {}

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(ORDER_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest(orderTableId, orderLineItems))
                .when().post(ORDER_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, OrderStatus orderStatus) {
                return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderStatusRequest(orderStatus.name()))
                .when().put(ORDER_PATH + "/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public static OrderResponse 주문_등록되어_있음(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        ExtractableResponse<Response> response = 주문_생성_요청(orderTableId, orderLineItems);
        주문_생성됨(response);
        return response.as(OrderResponse.class);
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_생성_주문상태_확인(ExtractableResponse<Response> response) {
        String actual = response.jsonPath()
                .getString("orderStatus");
        assertThat(actual).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 주문_상태_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, OrderResponse... orderResponses) {
        List<Long> actual = response.jsonPath()
                .getList("id", Long.class);
        List<Long> expect = Arrays.stream(orderResponses)
                .map(OrderResponse::getId)
                .collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expect);
    }
}
