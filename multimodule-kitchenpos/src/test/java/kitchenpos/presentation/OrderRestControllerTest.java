package kitchenpos.presentation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.testassistance.config.TestConfig;

@DisplayName("주문 API기능에 관한")
public class OrderRestControllerTest extends TestConfig {
    @DisplayName("주문이 저장된다.")
    @Test
    void save_order() {
        // given
        OrderDto order = 저장될_주문생성();

        // when
        ExtractableResponse<Response> response = 주문_저장요청(order);

        // then
        주문_저장됨(response);
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        // when
        ExtractableResponse<Response> response = 주문_조회요청();

        // then
        주문_조회됨(response);
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderState() {
        // given
        OrderDto order = 저장될_주문생성();

        // when
        OrderDto createdOrder = 주문_저장요청(order).as(OrderDto.class);

        // given
        createdOrder.changeOrderStatus(OrderStatus.MEAL);

        // when
        ExtractableResponse<Response> response = 주문_상태변경요청(createdOrder);

        // then
        주문_상태변경됨(response);
    }

    public static OrderDto 저장될_주문생성() {
        List<OrderLineItemDto> orderLineItems = 주문명세서_생성();

        return OrderDto.of(9L, orderLineItems);
    }

    public static List<OrderLineItemDto> 주문명세서_생성() {
        return List.of(OrderLineItemDto.of(1L,1L), OrderLineItemDto.of(2L,1L));
    }

    private void 주문_저장됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 주문_저장요청(OrderDto order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문_조회요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 주문_상태변경됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_상태변경요청(OrderDto order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/" + order.getId() + "/order-status")
                .then().log().all()
                .extract();
    }
}
