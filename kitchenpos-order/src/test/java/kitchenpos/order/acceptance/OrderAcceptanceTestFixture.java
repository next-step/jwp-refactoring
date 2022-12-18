package kitchenpos.order.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTestFixture {
    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성되어_있음(OrderRequest request) {
        return 주문_생성_요청(request);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderStatus expectedStatus) {
        OrderStatusChangeRequest request = new OrderStatusChangeRequest(expectedStatus);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> responses) {
        List<Long> expectedIds = responses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList(".", OrderResponse.class).stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectedIds);
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus expectedStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(expectedStatus.name());
    }
}
