package kitchenpos.order.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.util.OrderRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kitchenpos.table.ui.OrderTableAcceptanceTest.테이블_상태_변경_요청;
import static kitchenpos.utils.ResponseUtil.getLocationCreatedId;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getLocationCreatedId(response)).isNotNull();
    }

    @DisplayName("주문의 메뉴는 1개 이상이어야 한다.")
    @Test
    void createOrderInMenuGraterThanOne() {
        // when
        ExtractableResponse<Response> response = 주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블이 비어있지 않으면 주문이 불가능하다.")
    @Test
    void expectedExceptionNotEmptyTable() {
        테이블_상태_변경_요청(1L, false);
        // when
        ExtractableResponse<Response> response = 주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문 상태 변경 요청")
    @Test
    void changeOrderStatus() {
        long createdOrderId = getLocationCreatedId(주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build()));

        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(createdOrderId, "MEAL");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo("MEAL");
    }

    @DisplayName("완료된 주문 상태 변경 요청")
    @Test
    void expectedExceptionAlreadyCompleteOrderStatus() {
        // given
        long createdOrderId = getLocationCreatedId(주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build()));
        주문_상태_변경_요청(createdOrderId, Order.OrderStatus.COMPLETION.name());

        // when then
        ExtractableResponse<Response> response = 주문_상태_변경_요청(createdOrderId, Order.OrderStatus.MEAL.name());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(long createdOrderId, String status) {
        Map<String, String> params = new HashMap<>();
        params.put("orderStatus", status);
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("api/orders/{id}/order-status", createdOrderId).
                then().
                log().all().
                extract();
    }

    @DisplayName("주문 목록을 가져올 수 있다.")
    @Test
    void findAllOrders() {
        // given
        주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(1L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        주문_요청됨(new OrderRequestBuilder()
                .withOrderTableId(2L)
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        assertThat(findOrderIds(response)).hasSize(2);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all().
                when().
                get("/api/orders").
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문_요청됨(OrderRequest params) {
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/orders").
                then().
                log().all().
                extract();
    }

    private List<Long> findOrderIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", OrderResponse.class).stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());
    }
}
