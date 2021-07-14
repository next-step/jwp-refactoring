package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문_테이블;
    private MenuResponse 메뉴1;
    private MenuResponse 메뉴2;

    private OrderResponse 주문;

    @BeforeEach
    void setUpData() {
        OrderTableRequest request = new OrderTableRequest(3, false);
        주문_테이블 = OrderTableAcceptanceTest
            .주문_테이블_생성_성공(OrderTableAcceptanceTest.주문_테이블_생성_요청(request));

        MenuGroupRequest 메뉴그룹_요청 = new MenuGroupRequest("메뉴그룹");
        ExtractableResponse<Response> 메뉴그룹_요청_결과 = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청(메뉴그룹_요청);
        MenuGroupResponse 메뉴그룹 = MenuGroupAcceptanceTest.메뉴_그룹_생성_성공(메뉴그룹_요청_결과);

        ProductRequest 제품_요청_1 = new ProductRequest("제품1", BigDecimal.valueOf(1000L));
        ProductResponse 제품1 = ProductAcceptanceTest
            .제품_생성_성공(ProductAcceptanceTest.제품_생성_요청(제품_요청_1));

        ProductRequest 제품_요청_2 = new ProductRequest("제품2", BigDecimal.valueOf(2000L));
        ProductResponse 제품2 = ProductAcceptanceTest
            .제품_생성_성공(ProductAcceptanceTest.제품_생성_요청(제품_요청_2));

        MenuRequest menuRequest = MenuRequest.Builder.of("맛좋은테스트메뉴", BigDecimal.valueOf(2000L))
                                                     .menuGroupId(메뉴그룹.getId())
                                                     .menuProducts(Arrays.asList(
                                                         new MenuProductRequest(제품1.getId(), 2),
                                                         new MenuProductRequest(제품2.getId(), 4)))
                                                     .build();
        메뉴1 = MenuAcceptanceTest.메뉴_생성_성공(MenuAcceptanceTest.메뉴_생성_요청(menuRequest));
        메뉴2 = MenuAcceptanceTest.메뉴_생성_성공(MenuAcceptanceTest.메뉴_생성_요청(menuRequest));

        OrderRequest orderRequest = new OrderRequest(주문_테이블.getId(),
                                                     Arrays.asList(
                                                         new OrderLineItemRequest(메뉴1.getId(), 3),
                                                         new OrderLineItemRequest(메뉴2.getId(), 5)));
        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        주문 = 주문_생성_성공(response);
    }


    @DisplayName("주문 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        OrderRequest orderRequest = new OrderRequest(주문_테이블.getId(),
                                                     Arrays.asList(
                                                         new OrderLineItemRequest(메뉴1.getId(), 3),
                                                         new OrderLineItemRequest(메뉴2.getId(), 5)));

        // when
        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        // then
        OrderResponse actual = 주문_생성_성공(response);
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getOrderTableId()).isEqualTo(주문_테이블.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems()
                             .stream()
                             .map(OrderLineItemResponse::getMenuId)
                             .collect(Collectors.toList()))
                .hasSize(2)
                .containsExactly(메뉴1.getId(), 메뉴2.getId());
        });
    }

    @DisplayName("전체 주문 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        ExtractableResponse<Response> response = 전체_주문_조회_요청();

        // then
        전체_주문_조회_성공(response);
    }

    @DisplayName("주문 상태 변경 통합 테스트")
    @Test
    void changeOrderStatusTest() {
        // when
        ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), new OrderStatusRequest(OrderStatus.MEAL));

        // then
        OrderResponse actual = 주문_상태_변경_성공(response);
        assertThat(actual).isNotNull()
                          .extracting(OrderResponse::getOrderStatus)
                          .isEqualTo(OrderStatus.MEAL.name());
    }

    public static ExtractableResponse<Response> 주문_생성_요청(final OrderRequest request) {
        // when
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/api/orders")
                          .then().log().all().extract();
    }

    public static OrderResponse 주문_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderResponse.class);
    }

    public static ExtractableResponse<Response> 전체_주문_조회_요청() {
        return RestAssured.given().log().all()
                          .when().get("/api/orders")
                          .then().log().all().extract();
    }

    public static void 전체_주문_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final Long id,
                                                            final OrderStatusRequest request) {
        // when
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().put(String.format("/api/orders/%s/order-status", id))
                          .then().log().all().extract();
    }

    public static OrderResponse 주문_상태_변경_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(OrderResponse.class);
    }
}
