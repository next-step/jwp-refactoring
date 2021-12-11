package kitchenpos.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.dto.OrderTableDto;
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

    private OrderDto 저장될_주문생성() {
        OrderTableDto orderTable = 반테이블들_조회됨().get(0);
        orderTable.changeNumberOfGuests(10);

        OrderTableDto changedOrderTable = TableRestControllerTest.주문테이블_빈테이블_변경요청(orderTable).as(OrderTableDto.class);

        MenuDto[] menus = MenuRestControllerTest.메뉴_조회요청().as(MenuDto[].class);

        List<OrderLineItemDto> orderLineItems = 주문명세서_생성(List.of(menus[0], menus[1]));

        return OrderDto.of(changedOrderTable.getId(), orderLineItems);
    }

    private List<OrderLineItemDto> 주문명세서_생성(List<MenuDto> menus) {
        List<OrderLineItemDto> orderLineItems = new ArrayList<>();

        for (final MenuDto menu : menus) {
            orderLineItems.add(OrderLineItemDto.of(menu.getId(), 1L));
        }

        return orderLineItems;
    }

    private List<OrderTableDto> 반테이블들_조회됨() {
        return List.of(TableRestControllerTest.주문테이블_조회요청().as(OrderTableDto[].class)).stream()
                                .filter(OrderTableDto::isEmpty)
                                .collect(Collectors.toList());
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
